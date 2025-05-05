package dev.enricosola.porcellino.dto;

import dev.enricosola.porcellino.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserTokenResponseDTO extends ResponseDTO {
    public static UserTokenResponseDTO fromUserTokenDTO(UserTokenDTO userTokenDTO) {
        return UserTokenResponseDTO.builder()
                .expiration(userTokenDTO.getExpiration())
                .scopes(userTokenDTO.getScopes())
                .token(userTokenDTO.getToken())
                .build();
    }

    private final String token;
    private final Date expiration;
    private final String[] scopes;
}
