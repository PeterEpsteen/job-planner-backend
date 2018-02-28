package api.controllers;

import api.models.Job;
import api.models.Login;
import api.models.PasswordEncoder;
import api.models.User;
import api.repository.JobRepository;
import api.repository.UserRepository;
import api.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    private PasswordEncoder passwordEncoder;

    @RequestMapping("/all")
    public @ResponseBody  Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody User user) {
        if (user.getPassword().length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please enter a valid password");
        }
        passwordEncoder = new PasswordEncoder();
        String hashed = passwordEncoder.passwordEncoder().encode(user.getPassword());
        user.setPassword(hashed);
        userRepository.save(user);
        return ResponseEntity.status(200).body("Inserted User");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity login(@RequestBody Login login) {
        passwordEncoder = new PasswordEncoder();
        User user = userRepository.findByUsername(login.getUsername());
        if (user != null) {
            if(passwordEncoder.passwordEncoder().matches(login.getPassword(),
                    user.getPassword()))
            {
                return ResponseEntity.status(200).body(Jwts.builder().setSubject(
                        String.valueOf(user.getId())).signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                        .compact()
                );
            }
        }
        return ResponseEntity.status(500).body("Invalid Login");
    }

    @GetMapping("/details")
    public @ResponseBody ResponseEntity getUser(@RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        if(user != null)
        {
            return ResponseEntity.status(200).body(user);
        }
        return ResponseEntity.status(500).body("Token invalid");
    }

    @PostMapping("/jobs")
    public @ResponseBody ResponseEntity addJob(@RequestBody Job job) {
        jobRepository.save(job);
        if(jobRepository.findAll().size() > 0) {
            return ResponseEntity.status(200).body("success");
        }
        return ResponseEntity.status(500).body("Try again");
    }


    @GetMapping("/jobs")
    public @ResponseBody Iterable<Job>  getAll() {
       return jobRepository.findAll();
    }



//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public @ResponseBody String login(@ResponseBody User user) {
//
//    }
}
