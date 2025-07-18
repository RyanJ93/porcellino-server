package dev.enricosola.porcellino.dto.response.user;

import dev.enricosola.porcellino.dto.TwoFactorAuthSetupWithQRCodeDTO;
import dev.enricosola.porcellino.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.io.Serial;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TwoFactorAuthSetupWithQRCodeResponseDTO extends ResponseDTO implements BaseTwoFactorAuthSetupResponseDTO {
    @Serial
    private static final long serialVersionUID = -7418348061359855729L;

    public static TwoFactorAuthSetupWithQRCodeResponseDTO fromTwoFactorAuthSetupWithQRCodeDTO(TwoFactorAuthSetupWithQRCodeDTO twoFactorAuthSetupWithQRCodeDTO) {
        return TwoFactorAuthSetupWithQRCodeResponseDTO.builder()
                .QRCode(twoFactorAuthSetupWithQRCodeDTO.getQRCode())
                .secret(twoFactorAuthSetupWithQRCodeDTO.getSecret())
                .label(twoFactorAuthSetupWithQRCodeDTO.getLabel())
                .url(twoFactorAuthSetupWithQRCodeDTO.getUrl())
                .build();
    }

    protected final String secret;
    protected final String label;
    protected final String url;
    protected final String QRCode;
}
