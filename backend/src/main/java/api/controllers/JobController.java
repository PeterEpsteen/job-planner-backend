package api.controllers;

import api.models.*;
import api.repository.*;
import api.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
//TODO update the errors

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
    @Autowired
    EventRepository eventRepository;
    @Autowired
    ContactRepository contactRepository;


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

    @PostMapping("/event/{jobId}")
    public @ResponseBody ResponseEntity<Event> addEvent(@PathVariable long jobId, @RequestBody Event event, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() != userID) {
            return ResponseEntity.notFound().build();
        }
        event.setJob(job);
        eventRepository.save(event);
        return ResponseEntity.ok().body(event);
    }
    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.DELETE)
    public ResponseEntity<Event> deleteEvent(@PathVariable long eventId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Event toDelete = eventRepository.findById(eventId);
        if (toDelete.getJob().getUser().getId() != userID) {
            return ResponseEntity.badRequest().build();
        }
        eventRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);

    }

    @PostMapping("/contact/{jobId}")
    public @ResponseBody ResponseEntity<JobContact> addContact(@PathVariable long jobId, @RequestBody JobContact contact, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() != userID) {
            return ResponseEntity.notFound().build();
        }
        contact.setJob(job);
        contactRepository.save(contact);
        return ResponseEntity.ok().body(contact);
    }
    @RequestMapping(value = "/contact/{contactId}", method = RequestMethod.DELETE)
    public ResponseEntity<JobContact> deleteContact(@PathVariable long contactId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        JobContact toDelete = contactRepository.findById(contactId);
        if (toDelete.getJob().getUser().getId() != userID) {
            return ResponseEntity.badRequest().build();
        }
        contactRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);

    }




    @PostMapping("/todo/{jobId}")
    public @ResponseBody ResponseEntity<Todo> addTodo(@PathVariable long jobId, @RequestBody Todo todo, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() != userID) {
            return ResponseEntity.notFound().build();
        }
        todo.setJob(job);
        todoRepo.save(todo);
        return ResponseEntity.ok().body(todo);
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
    @PutMapping("/todo")
    public ResponseEntity<Todo> updateTodo(@Valid @RequestBody Todo newTodo, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Todo todo = todoRepo.findById(newTodo.getId());
        if(todo.getJob().getUser().getId() != userID) {
            return ResponseEntity.badRequest().build();
        }
        todo.setComplete(newTodo.getComplete());
        Todo updatedTodo = todoRepo.save(todo);
        return ResponseEntity.ok(updatedTodo);
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
