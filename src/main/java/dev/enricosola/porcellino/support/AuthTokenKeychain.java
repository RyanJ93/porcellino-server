package dev.enricosola.porcellino.support;

import dev.enricosola.porcellino.dto.UserTokenDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AuthTokenKeychain {
    private UserTokenDTO refreshToken;
    private UserTokenDTO accessToken;
}
