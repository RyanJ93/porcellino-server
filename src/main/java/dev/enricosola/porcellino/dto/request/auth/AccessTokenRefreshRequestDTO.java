package dev.enricosola.porcellino.dto.request.auth;

import dev.enricosola.porcellino.dto.DTO;
import lombok.Data;

@Data
public class AccessTokenRefreshRequestDTO implements DTO {
    private String refreshToken;
}
