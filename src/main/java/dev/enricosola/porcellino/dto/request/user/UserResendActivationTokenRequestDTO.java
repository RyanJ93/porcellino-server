package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.user.UserResendActivationTokenDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = false)
@Data
public class UserResendActivationTokenRequestDTO extends RequestDTO {
    @NotBlank(message = "You must provide your e-mail address.")
    @Email(message = "You must provide your e-mail address.")
    protected String email;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResendActivationTokenDTO toServiceDTO() {
        return new UserResendActivationTokenDTO(this.email);
    }
}
