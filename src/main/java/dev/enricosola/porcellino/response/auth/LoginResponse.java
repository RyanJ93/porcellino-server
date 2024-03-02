package dev.enricosola.porcellino.response.auth;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class LoginResponse extends SuccessResponse implements Serializable, Response {
    @Serial
    private static final long serialVersionUID = -2661795404867858389L;

    private final String token;

    public LoginResponse(String token){
        super(null);

        this.token = token;
    }
}
