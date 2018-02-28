//package api.controllers;
//
//import api.models.User;
//import api.repository.UserRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Collections;
//
//public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
//    private UserRepository userRepository;
//
//    public UserDetailsServiceImpl(UserRepository repository) {
//        this.userRepository = repository;
//    }
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
//    }
//}
