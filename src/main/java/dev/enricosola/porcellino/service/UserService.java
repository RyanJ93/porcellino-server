package dev.enricosola.porcellino.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.repository.UserRepository;
import dev.enricosola.porcellino.form.user.SignupForm;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import org.slf4j.Logger;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Getter
    @Setter
    private User user;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByEmail(String email){
        return this.userRepository.getUserByEmail(email);
    }

    public User createFromForm(SignupForm signupForm){
        return this.create(signupForm.getEmail(), signupForm.getPassword());
    }

    public User create(String email, String password){
        User user = new User();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(encodedPassword);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setEmail(email);
        this.user = this.userRepository.saveAndFlush(user);
        UserService.logger.info("Signed up user \"" + email + "\".");
        return this.user;
    }
}
