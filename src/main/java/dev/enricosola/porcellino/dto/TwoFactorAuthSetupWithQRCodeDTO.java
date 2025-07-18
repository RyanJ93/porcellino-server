package dev.enricosola.porcellino.dto;

import lombok.Getter;

public class TwoFactorAuthSetupWithQRCodeDTO extends TwoFactorAuthSetupDTO {
    /**
     * Creates a new instance of TwoFactorAuthSetupWithQRCodeDTO by combining the properties of the given TwoFactorAuthSetupDTO with a specified QR code.
     *
     * @param twoFactorAuthSetupDTO the TwoFactorAuthSetupDTO object containing the initial setup details such as secret, label, and URL.
     * @param QRCode the QR code string to be included in the returned object.
     * @return a new instance of TwoFactorAuthSetupWithQRCodeDTO combining the properties of the provided TwoFactorAuthSetupDTO and the given QR code.
     */
    public static TwoFactorAuthSetupWithQRCodeDTO fromTwoFactorAuthSetupDTO(TwoFactorAuthSetupDTO twoFactorAuthSetupDTO, String QRCode) {
        return new TwoFactorAuthSetupWithQRCodeDTO(
                twoFactorAuthSetupDTO.getSecret(),
                twoFactorAuthSetupDTO.getLabel(),
                twoFactorAuthSetupDTO.getUrl(),
                QRCode
        );
    }

    @Getter
    protected final String QRCode;

    public TwoFactorAuthSetupWithQRCodeDTO(String secret, String label, String url, String QRCode) {
        super(secret, label, url);

        this.QRCode = QRCode;
    }
}
