package api.controllers;

import api.models.*;
import api.repository.JobRepository;
import api.repository.UserRepository;
import api.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;
@CrossOrigin(origins = "http://localhost:4200")
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

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponse> login(@RequestBody Login login) {
        passwordEncoder = new PasswordEncoder();
        User user = userRepository.findByUsername(login.getUsername());
        if (user != null) {
            if(passwordEncoder.passwordEncoder().matches(login.getPassword(),
                    user.getPassword()))
            {
                return new ResponseEntity<TokenResponse>(new TokenResponse(Jwts.builder().setSubject(
                        String.valueOf(user.getId())).signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                        .compact()
                ), HttpStatus.OK);
            }
        }

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

}
