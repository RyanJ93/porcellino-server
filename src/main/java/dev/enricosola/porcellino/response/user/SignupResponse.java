package dev.enricosola.porcellino.response.user;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import dev.enricosola.porcellino.dto.UserDTO;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class SignupResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = 5639841085045500824L;

    protected final UserDTO user;

    public SignupResponse(UserDTO user){
        super(null);

        this.user = user;
    }
}
