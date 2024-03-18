package dev.enricosola.porcellino.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.repository.UserRepository;
import dev.enricosola.porcellino.form.user.SignupForm;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Transactional
@Service
@Slf4j
public class UserService {
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
        log.info("Signed up user \"" + email + "\".");
        return this.user;
    }
}
