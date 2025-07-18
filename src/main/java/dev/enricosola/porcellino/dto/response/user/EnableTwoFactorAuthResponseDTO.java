package dev.enricosola.porcellino.dto.response.user;

import dev.enricosola.porcellino.dto.TwoFactorAuthRecoveryCodeCollectionDTO;
import dev.enricosola.porcellino.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.io.Serial;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class EnableTwoFactorAuthResponseDTO extends ResponseDTO implements Serializable  {
    @Serial
    private static final long serialVersionUID = 5717086963334790396L;

    /**
     * Creates an instance of EnableTwoFactorAuthResponseDTO from the provided TwoFactorAuthRecoveryCodeCollectionDTO object.
     *
     * @param twoFactorAuthRecoveryCodeCollectionDTO the TwoFactorAuthRecoveryCodeCollectionDTO instance containing the recovery codes.
     * @return a new EnableTwoFactorAuthResponseDTO instance initialized with the recovery codes from the provided TwoFactorAuthRecoveryCodeCollectionDTO.
     */
    public static EnableTwoFactorAuthResponseDTO fromTwoFactorAuthRecoveryCodeCollectionDTO(
            TwoFactorAuthRecoveryCodeCollectionDTO twoFactorAuthRecoveryCodeCollectionDTO
    ) {
        return new EnableTwoFactorAuthResponseDTO(twoFactorAuthRecoveryCodeCollectionDTO.getRecoveryCodeList());
    }

    private final String[] recoveryCodeList;
}
