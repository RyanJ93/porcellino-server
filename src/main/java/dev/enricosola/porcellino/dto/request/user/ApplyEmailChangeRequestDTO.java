package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.user.ApplyEmailChangeDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApplyEmailChangeRequestDTO extends RequestDTO {
    @NotBlank(message = "You must provide a valid verification token.")
    private String token;


    @Override
    public ApplyEmailChangeDTO toServiceDTO() {
        return new ApplyEmailChangeDTO(this.token);
    }
}
