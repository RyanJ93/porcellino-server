package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.user.RequestPasswordResetDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestPasswordResetRequestDTO extends RequestDTO {
    @NotBlank(message = "You must provide your e-mail address.")
    @Email(message = "You must provide your e-mail address.")
    private String email;

    /**
     * Convert this DTO instance to another one that can be used in services.
     *
     * @return The generated DTO for service interaction.
     */
    public RequestPasswordResetDTO toServiceDTO() {
        return new RequestPasswordResetDTO(this.email);
    }
}
