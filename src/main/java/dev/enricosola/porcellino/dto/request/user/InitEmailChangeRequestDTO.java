package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.user.InitEmailChangeDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public class InitEmailChangeRequestDTO extends RequestDTO {
    @NotBlank(message = "You must provide your e-mail address.")
    @Email(message = "You must provide your e-mail address.")
    private String email;

    @Override
    public InitEmailChangeDTO toServiceDTO() {
        return new InitEmailChangeDTO(this.email);
    }
}
