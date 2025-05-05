package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.dto.ClientInfoDTO;
import dev.enricosola.porcellino.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserAuthDTO implements DTO {
    protected String email;
    protected String password;
    protected ClientInfoDTO clientInfoDTO;
}
