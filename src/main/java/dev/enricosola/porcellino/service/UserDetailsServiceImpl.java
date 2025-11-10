package dev.enricosola.porcellino.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.exception.LegacyNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserLookupService userLookupService;

    public UserDetailsServiceImpl(UserLookupService userLookupService) {
        this.userLookupService = userLookupService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new AuthenticatedUserDetails(this.userLookupService.findByEmail(username));
        } catch (LegacyNotFoundException ignored) {
            log.info("Access denied for user \"{}\": no matching user found.", username);
            throw new UsernameNotFoundException("No user matching the given email address found.");
        }
    }
}
