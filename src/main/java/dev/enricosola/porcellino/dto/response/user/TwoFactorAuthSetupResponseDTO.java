package dev.enricosola.porcellino.dto.response.user;

import dev.enricosola.porcellino.dto.TwoFactorAuthSetupDTO;
import dev.enricosola.porcellino.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import java.io.Serial;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TwoFactorAuthSetupResponseDTO extends ResponseDTO implements BaseTwoFactorAuthSetupResponseDTO {
    @Serial
    private static final long serialVersionUID = 1032544193148231460L;

    /**
     * Converts a TwoFactorAuthSetupDTO object into a TwoFactorAuthSetupResponseDTO object.
     *
     * @param twoFactorAuthSetupDTO the TwoFactorAuthSetupDTO instance containing the secret and label for two-factor authentication setup.
     * @return a TwoFactorAuthSetupResponseDTO instance built using the secret and label from the provided TwoFactorAuthSetupDTO.
     */
    public static TwoFactorAuthSetupResponseDTO fromTwoFactorAuthSetupDTO(TwoFactorAuthSetupDTO twoFactorAuthSetupDTO) {
        return TwoFactorAuthSetupResponseDTO.builder()
                .secret(twoFactorAuthSetupDTO.getSecret())
                .label(twoFactorAuthSetupDTO.getLabel())
                .url(twoFactorAuthSetupDTO.getUrl())
                .build();
    }

    protected final String secret;
    protected final String label;
    protected final String url;
}
