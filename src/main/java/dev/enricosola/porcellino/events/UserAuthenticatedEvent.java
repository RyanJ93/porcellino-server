package dev.enricosola.porcellino.events;

import org.springframework.context.ApplicationEvent;
import dev.enricosola.porcellino.entity.User;
import java.io.Serial;
import lombok.Getter;

@Getter
public class UserAuthenticatedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = -2670433987256155192L;

    private final User user;

    public UserAuthenticatedEvent(Object source, User user) {
        super(source);

        this.user = user;
    }
}
