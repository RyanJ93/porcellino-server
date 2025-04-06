package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.request.RequestDTO;
import dev.enricosola.porcellino.dto.user.UserActivateDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = false)
@Data
public class UserActivateRequestDTO extends RequestDTO {
    @NotBlank(message = "You must provide a valid verification token.")
    private String token;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserActivateDTO toServiceDTO() {
        return new UserActivateDTO(this.token);
    }
}
