package dev.enricosola.porcellino.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthenticatedUserDetails(this.userService.getUserByEmail(username).orElseThrow(() -> {
            log.info("Access denied for user \"" + username + "\": no matching user found.");
            return new UsernameNotFoundException("No user matching the given email address found.");
        }));
    }
}
