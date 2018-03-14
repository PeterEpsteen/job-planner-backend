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
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    private PasswordEncoder passwordEncoder;

    @RequestMapping("/all")
    public @ResponseBody  ResponseEntity<Iterable<User>> getAllUsers(@RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        if(user.getUsername().equals("PeterEps"))
            return ResponseEntity.ok(userRepository.findAll());
        return ResponseEntity.badRequest().build();
    }
    @CrossOrigin
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<User> signup(@RequestBody User user) {
        if (user.getPassword().length() < 6) {
            return ResponseEntity.badRequest().build();
        }
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().build();
        }
        passwordEncoder = new PasswordEncoder();
        String hashed = passwordEncoder.passwordEncoder().encode(user.getPassword());
        user.setPassword(hashed);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin
    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponse> login(@RequestBody Login login) {
        passwordEncoder = new PasswordEncoder();
        User user = userRepository.findByUsername(login.getUsername());
        if (user != null) {
            if(passwordEncoder.passwordEncoder().matches(login.getPassword(),
                    user.getPassword()))
            {
                return ResponseEntity.ok(new TokenResponse(Jwts.builder().setSubject(
                        String.valueOf(user.getId())).signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                        .compact()
                ));
            }
        }

            return ResponseEntity.badRequest().build();
    }
    @CrossOrigin
    @GetMapping("/details")
    public @ResponseBody ResponseEntity<User> getUser(@RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        if(user != null)
        {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
    @CrossOrigin
    @DeleteMapping("/{userId}")
    public @ResponseBody ResponseEntity<User> deleteUser(@PathVariable long userId, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        if(user != null && user.getId() == userId)
        {
            userRepository.delete(userID);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
    @CrossOrigin
    @PutMapping("/{userId}")
    public @ResponseBody ResponseEntity<User> editUser(@PathVariable long userId, @RequestBody User editedUser, @RequestHeader("x-access-token") String token) {
        passwordEncoder = new PasswordEncoder();
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        if(user != null && user.getId() == userId)
        {
            String hashed = passwordEncoder.passwordEncoder().encode(editedUser.getPassword());
            user.setPassword(hashed);
            user.setUsername(editedUser.getUsername());
            user.setFirstName(editedUser.getFirstName());
            user.setLastName(editedUser.getLastName());
            user.setEmail(editedUser.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
    @CrossOrigin
    @PostMapping("/jobs")
    public @ResponseBody ResponseEntity<Job> addJob(@RequestBody Job job, @RequestHeader("x-access-token") String token) {
        long userID = Long.parseLong(Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userID);
        job.setUser(user);
        jobRepository.save(job);
        return ResponseEntity.ok(job);


    }



}
