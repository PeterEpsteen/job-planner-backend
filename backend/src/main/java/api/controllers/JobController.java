package api.controllers;

import api.models.Job;
import api.models.User;
import api.repository.JobRepository;
import api.repository.UserRepository;
import api.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    UserRepository userRepository;


    @PostMapping("/add")
    public @ResponseBody ResponseEntity addJob(@RequestBody Job newJob, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = newJob;
        job.setUser(userRepository.findById(userID));
        jobRepository.save(job);
        return ResponseEntity.status(200).body("success");
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<Job> getJobsByUser(@RequestHeader("x-access-token") String token) {
        //check if no user
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        return jobRepository.findByUser(user);
    }
}
