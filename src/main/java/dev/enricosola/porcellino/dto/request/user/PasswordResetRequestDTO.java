package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.user.PasswordResetDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public class PasswordResetRequestDTO extends RequestDTO {
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters long.")
    @NotBlank(message = "You must provide a valid password.")
    private String password;

    @NotBlank(message = "You must provide a valid token.")
    private String token;

    /**
     * Convert this DTO instance to another one that can be used in services.
     *
     * @return The generated DTO for service interaction.
     */
    @Override
    public PasswordResetDTO toServiceDTO() {
        return new PasswordResetDTO(this.password, this.token);
    }
}
