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

@CrossOrigin
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

    @CrossOrigin
    @PostMapping("/add")
    public @ResponseBody ResponseEntity<Job> addJob(@RequestBody Job newJob, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = newJob;
        job.setUser(userRepository.findById(userID));
        jobRepository.save(job);
        return ResponseEntity.ok(job);
    }
    @CrossOrigin
    @GetMapping("/all")
    public @ResponseBody Iterable<Job> getJobsByUser(@RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        return jobRepository.findByUser(user);
    }
    @CrossOrigin
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
    @CrossOrigin
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
    @CrossOrigin
    @PostMapping("/contact/{jobId}")
    public @ResponseBody ResponseEntity<JobContact> addContact(@PathVariable long jobId, @RequestBody JobContact contact, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() != userID) {
            return ResponseEntity.notFound().build();
        }
        contact.setJob(job);
        contactRepository.save(contact);
        return ResponseEntity.ok(contact);
    }
    @CrossOrigin
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



    @CrossOrigin
    @PostMapping("/todo/{jobId}")
    public @ResponseBody ResponseEntity<Todo> addTodo(@PathVariable long jobId, @RequestBody Todo todo, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() != userID) {
            return ResponseEntity.notFound().build();
        }
        todo.setJob(job);
        todoRepo.save(todo);
        return ResponseEntity.ok(todo);
    }

    @CrossOrigin
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
    @CrossOrigin
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
    @CrossOrigin
    @RequestMapping(value = "/todo/{todoId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTodo(@PathVariable long todoId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        if (todoRepo.findById(todoId).getJob().getUser().getId() != userID) {
            return ResponseEntity.badRequest().build();
        }
        todoRepo.delete(todoId);
        return ResponseEntity.ok(todoId);

    }
    @CrossOrigin
    @RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
    public ResponseEntity<Job> getJob(@PathVariable long jobId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job job = jobRepository.findById(jobId);
        if(job.getUser().getId() == userID) {
            return ResponseEntity.ok(job);
        }
        else
            return ResponseEntity.badRequest().build();
    }
    @CrossOrigin
    @RequestMapping(value = "/{jobId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteJob(@PathVariable long jobId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        if (jobRepository.findById(jobId).getUser().getId() != userID) {
            return ResponseEntity.badRequest().build();
        }
        jobRepository.delete(jobId);
        return ResponseEntity.ok(jobId);

    }

    @CrossOrigin
    @PutMapping("{jobId}")
    public ResponseEntity<Job> editJob(@PathVariable long jobId, @RequestHeader("x-access-token") String token,
                                       @Valid @RequestBody Job editedJob) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        Job oldJob = jobRepository.findById(jobId);
        if(oldJob.getUser().getId() != userID) {
            return ResponseEntity.badRequest().build();
        }
        oldJob.setDescription(editedJob.getDescription());
        oldJob.setLocation(editedJob.getLocation());
        oldJob.setTitle(editedJob.getTitle());
        return ResponseEntity.ok(jobRepository.save(oldJob));
    }


}
