package dev.enricosola.porcellino.dto.request.auth;

import dev.enricosola.porcellino.dto.auth.TwoFactorRecoveryCodeChallengeDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import dev.enricosola.porcellino.dto.ClientInfoDTO;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = false)
@Data
public class TwoFactorRecoveryCodeChallengeRequestDTO extends RequestDTO {
    @NotBlank(message = "You must provide a valid recovery code.")
    @Length(min = 32, max = 32, message = "The recovery code must be 32 characters long.")
    private String recoveryCode;

    /**
     * {@inheritDoc}
     */
    @Override
    public TwoFactorRecoveryCodeChallengeDTO toServiceDTO() {
        return new TwoFactorRecoveryCodeChallengeDTO(this.recoveryCode, null);
    }

    /**
     * {@inheritDoc}
     */
    public TwoFactorRecoveryCodeChallengeDTO toServiceDTO(ClientInfoDTO clientInfoDTO) {
        return new TwoFactorRecoveryCodeChallengeDTO(this.recoveryCode, clientInfoDTO);
    }
}
