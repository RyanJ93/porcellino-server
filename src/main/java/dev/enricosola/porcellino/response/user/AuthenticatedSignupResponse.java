package dev.enricosola.porcellino.response.user;

import dev.enricosola.porcellino.dto.UserDTO;
import java.io.Serial;
import lombok.Getter;

@Getter
public class AuthenticatedSignupResponse extends SignupResponse {
    @Serial
    private static final long serialVersionUID = 3551057379404861582L;

    private final String token;

    public AuthenticatedSignupResponse(UserDTO user, String token){
        super(user);

        this.token = token;
    }
}
