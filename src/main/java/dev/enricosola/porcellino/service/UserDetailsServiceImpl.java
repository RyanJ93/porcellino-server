package dev.enricosola.porcellino.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthenticatedUserDetails(this.userService.getUserByEmail(username).orElseThrow(() -> {
            UserDetailsServiceImpl.logger.info("Access denied for user \"" + username + "\": no matching user found.");
            return new UsernameNotFoundException("No user matching the given email address found.");
        }));
    }
}
