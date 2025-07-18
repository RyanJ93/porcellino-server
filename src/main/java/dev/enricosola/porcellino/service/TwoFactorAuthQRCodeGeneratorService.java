package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.dto.TwoFactorAuthSetupDTO;
import dev.enricosola.porcellino.exception.TwoFactorQRCodeGenerationException;
import org.springframework.stereotype.Service;
import com.google.zxing.WriterException;
import java.io.IOException;

@Service
public class TwoFactorAuthQRCodeGeneratorService {
    private static final int QR_CODE_SIZE = 512;

    private final QRCodeService qrCodeService;

    public TwoFactorAuthQRCodeGeneratorService(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * Generates a QR code as a Base64-encoded PNG image string using the provided {@link TwoFactorAuthSetupDTO} object.
     * The URL from the two-factor authentication setup DTO is used to generate the QR code.
     *
     * @param twoFactorAuthSetupDTO an instance of {@link TwoFactorAuthSetupDTO}, containing information for generating a two-factor authentication (2FA) setup QR code
     * @return a Base64-encoded string representing the generated QR code as a PNG image
     */
    public String generateQRCode(TwoFactorAuthSetupDTO twoFactorAuthSetupDTO) {
        try {
            return this.qrCodeService.generateAsBase64(twoFactorAuthSetupDTO.getUrl(), TwoFactorAuthQRCodeGeneratorService.QR_CODE_SIZE);
        } catch (WriterException|IOException ex) {
            throw new TwoFactorQRCodeGenerationException("Unable to generate QR code for Two-Factor Auth Setup", ex);
        }
    }
}
