package dev.enricosola.porcellino.dto.response.auth;

import dev.enricosola.porcellino.dto.response.ResponseDTO;
import dev.enricosola.porcellino.dto.UserTokenResponseDTO;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import lombok.Builder;
import java.io.Serial;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LoginResponseDTO extends ResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7401564670384735922L;

    private final UserTokenResponseDTO accessToken;
    private final UserTokenResponseDTO refreshToken;
}
