package dev.enricosola.porcellino.dto.response.auth;

import dev.enricosola.porcellino.dto.UserTokenResponseDTO;
import dev.enricosola.porcellino.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import lombok.Builder;
import java.io.Serial;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TokenRefreshResponseDTO extends ResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7861515317242951959L;

    private final UserTokenResponseDTO accessToken;
    private final UserTokenResponseDTO refreshToken;
}
