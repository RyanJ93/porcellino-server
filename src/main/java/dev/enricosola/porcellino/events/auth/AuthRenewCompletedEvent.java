package dev.enricosola.porcellino.events.auth;

import org.springframework.context.ApplicationEvent;
import dev.enricosola.porcellino.entity.User;
import java.io.Serial;
import lombok.Getter;

@Getter
public class AuthRenewCompletedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 784635662476522278L;

    private final User user;

    public AuthRenewCompletedEvent(Object source, User user) {
        super(source);

        this.user = user;
    }
}
