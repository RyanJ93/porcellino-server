package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordUpdateDTO implements DTO {
    private String oldPassword;
    private String newPassword;
}
