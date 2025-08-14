package dev.enricosola.porcellino.policy;

import dev.enricosola.porcellino.facades.Auth;
import dev.enricosola.porcellino.entity.User;

public abstract class BasePolicy implements Policy {
    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the authenticated User object.
     */
    protected User getAuthenticatedUser() {
        return Auth.getUser();
    }
}
