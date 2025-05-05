package dev.enricosola.porcellino.events.auth;

import org.springframework.context.ApplicationEvent;
import dev.enricosola.porcellino.entity.User;
import java.io.Serial;
import lombok.Getter;

@Getter
public class AuthRevokedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = -3514244578294067417L;

    private final User user;

    public AuthRevokedEvent(Object source, User user) {
        super(source);

        this.user = user;
    }
}
