package dev.enricosola.porcellino.events.auth;

import org.springframework.context.ApplicationEvent;
import dev.enricosola.porcellino.entity.User;
import java.io.Serial;
import lombok.Getter;

@Getter
public class AuthCompletedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = -7961715052275288223L;

    private final User user;

    public AuthCompletedEvent(Object source, User user) {
        super(source);

        this.user = user;
    }
}
