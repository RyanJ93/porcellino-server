package dev.enricosola.porcellino.notifications.user;

import dev.enricosola.porcellino.notifications.EmailNotification;
import dev.enricosola.porcellino.entity.User;

public abstract class UserEmailNotification extends EmailNotification {
    protected final User user;

    /**
     * The class constructor.
     *
     * @param user The user this notification will be delivered to.
     */
    protected UserEmailNotification(User user) {
        this.user = user;
    }
}
