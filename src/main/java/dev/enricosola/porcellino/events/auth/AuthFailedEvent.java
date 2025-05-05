package dev.enricosola.porcellino.events.auth;

import dev.enricosola.porcellino.enums.AuthFailReason;
import org.springframework.context.ApplicationEvent;
import java.io.Serial;
import lombok.Getter;

@Getter
public class AuthFailedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = -2503267074749298657L;

    private final AuthFailReason authFailReason;
    private final String email;

    public AuthFailedEvent(Object source, AuthFailReason authFailReason, String email) {
        super(source);

        this.authFailReason = authFailReason;
        this.email = email;
    }
}
