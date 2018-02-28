package api.controllers;

import api.models.Job;
import api.models.User;
import api.repository.JobRepository;
import api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/{user_id}")
    public @ResponseBody Iterable<Job> getJobsByUser(@PathVariable("user_id") long id) {
        User user = userRepository.findById(id);
        return jobRepository.findByUser(user);
    }

    @PostMapping("/add")
    public @ResponseBody ResponseEntity addJob(@RequestBody Job job) {
        jobRepository.save(job);
        return ResponseEntity.status(200).body("success");
    }
}
