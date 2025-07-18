package dev.enricosola.porcellino.dto.request.auth;

import dev.enricosola.porcellino.dto.auth.TwoFactorCodeChallengeDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import dev.enricosola.porcellino.dto.ClientInfoDTO;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public class TwoFactorCodeChallengeRequestDTO extends RequestDTO {
    @NotBlank(message = "You must provide a valid OTP code.")
    @Length(min = 6, max = 6, message = "The OTP code must be 6 digits long.")
    private String code;

    /**
     * {@inheritDoc}
     */
    @Override
    public TwoFactorCodeChallengeDTO toServiceDTO() {
        return new TwoFactorCodeChallengeDTO(this.code, null);
    }

    /**
     * {@inheritDoc}
     */
    public TwoFactorCodeChallengeDTO toServiceDTO(ClientInfoDTO clientInfoDTO) {
        return new TwoFactorCodeChallengeDTO(this.code, clientInfoDTO);
    }
}
