package dev.enricosola.porcellino.response.auth;

import dev.enricosola.porcellino.dto.AuthenticationContract;
import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import dev.enricosola.porcellino.entity.User;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class LoginResponse extends SuccessResponse implements Serializable, Response {
    @Serial
    private static final long serialVersionUID = -2661795404867858389L;

    private final String token;
    private final User user;

    public LoginResponse(AuthenticationContract authenticationContract){
        super(null);

        this.token = authenticationContract.getToken();
        this.user = authenticationContract.getUser();
    }

    public LoginResponse(User user, String token){
        super(null);

        this.token = token;
        this.user = user;
    }
}
