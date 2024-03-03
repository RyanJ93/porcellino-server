package dev.enricosola.porcellino.dto;

import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationContract {
    private String token;
    private User user;
}
