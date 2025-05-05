package dev.enricosola.porcellino.dto;

import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class UserTokenDTO implements DTO {
    protected final String token;
    protected final User user;
    protected final Date expiration;
    protected final String[] scopes;
    protected final Map<String, String> payload;
}
