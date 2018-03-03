package api.controllers;

import api.models.Job;
import api.models.Todo;
import api.models.User;
import api.repository.JobRepository;
import api.repository.TodoRepository;
import api.repository.UserRepository;
import api.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TodoRepository todoRepo;


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

    @PostMapping("/todo/{jobId}")
    public @ResponseBody ResponseEntity addTodo(@PathVariable long jobId, @RequestBody Todo todo, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() != userID) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        todo.setJob(job);
        todoRepo.save(todo);
        return ResponseEntity.status(200).body("success");
    }
    @RequestMapping(value = "/todo/{jobId}", method = RequestMethod.GET)
    public Iterable<Todo> getTodos(@PathVariable long jobId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() == userID) {
            return null;
        }
        else
            return todoRepo.findByJob(job);
    }
    @RequestMapping(value = "/todo/{todoId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTodo(@PathVariable long todoId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        if (todoRepo.findById(todoId).getJob().getUser().getId() != userID) {
            return null;
        }
        todoRepo.delete(todoId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted it.");

    }

    @RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
    public Job getJob(@PathVariable long jobId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() == userID) {
            return job;
        }
        else
            return new Job();
    }


}
