package dev.enricosola.porcellino.support;

import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AuthenticationContract {
    private User user;
    private AuthTokenKeychain authTokenKeychain;
}
