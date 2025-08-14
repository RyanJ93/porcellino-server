package dev.enricosola.porcellino.facades;

import org.springframework.security.core.context.SecurityContextHolder;
import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.entity.User;
import org.springframework.http.HttpStatus;

public class Auth {
    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the authenticated User object.
     * @throws ResponseStatusException if the user is not authenticated.
     */
    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUserDetails authenticatedUserDetails = (AuthenticatedUserDetails) authentication.getPrincipal();
        if (authenticatedUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated.");
        }
        return authenticatedUserDetails.getUser();
    }
}
