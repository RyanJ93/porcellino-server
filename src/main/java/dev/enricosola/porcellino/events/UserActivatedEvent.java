package dev.enricosola.porcellino.events;

import org.springframework.context.ApplicationEvent;
import dev.enricosola.porcellino.entity.User;
import java.io.Serial;
import lombok.Getter;

@Getter
public class UserActivatedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = -84981300248167723L;

    private final User user;

    public UserActivatedEvent(Object source, User user) {
        super(source);

        this.user = user;
    }
}
