package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.request.RequestDTO;
import dev.enricosola.porcellino.dto.user.UserCreateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = false)
@Data
public class UserSignupRequestDTO extends RequestDTO {
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters long.")
    @NotBlank(message = "You must provide a valid password.")
    protected String password;

    @NotBlank(message = "You must provide your e-mail address.")
    @Email(message = "You must provide your e-mail address.")
    protected String email;

    /**
     * {@inheritDoc}
     */
    public UserCreateDTO toServiceDTO(){
        return new UserCreateDTO(this.email, this.password);
    }
}
