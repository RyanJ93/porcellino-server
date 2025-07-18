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
public class RotateTwoFactorAuthRecoveryCodesResponseDTO extends ResponseDTO implements Serializable  {
    @Serial
    private static final long serialVersionUID = 8418155635235988641L;

    /**
     * Converts a TwoFactorAuthRecoveryCodeCollectionDTO object into a RotateTwoFactorAuthRecoveryCodesResponseDTO object.
     *
     * @param twoFactorAuthRecoveryCodeCollectionDTO the TwoFactorAuthRecoveryCodeCollectionDTO instance containing the recovery codes.
     * @return a new RotateTwoFactorAuthRecoveryCodesResponseDTO instance initialized with the recovery codes from the provided TwoFactorAuthRecoveryCodeCollectionDTO.
     */
    public static RotateTwoFactorAuthRecoveryCodesResponseDTO fromTwoFactorAuthRecoveryCodeCollectionDTO(
            TwoFactorAuthRecoveryCodeCollectionDTO twoFactorAuthRecoveryCodeCollectionDTO
    ) {
        return new RotateTwoFactorAuthRecoveryCodesResponseDTO(twoFactorAuthRecoveryCodeCollectionDTO.getRecoveryCodeList());
    }

    private final String[] recoveryCodeList;
}
