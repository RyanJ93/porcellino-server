package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.auth.twofactor.NotInitializedTwoFactorAuthException;
import dev.enricosola.porcellino.exception.auth.twofactor.InvalidCodeTwoFactorAuthException;
import dev.enricosola.porcellino.dto.TwoFactorAuthRecoveryCodeCollectionDTO;
import dev.enricosola.porcellino.dto.TwoFactorAuthSetupWithQRCodeDTO;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.dto.TwoFactorAuthSetupDTO;
import dev.enricosola.porcellino.entity.RecoveryCode;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import com.amdelamar.jotp.type.Type;
import com.amdelamar.jotp.OTP;
import java.io.IOException;
import java.util.List;

@Service
public class TwoFactorAuthService {
    private static final int RECOVERY_CODE_AMOUNT = 8;
    private static final String LABEL = "Porcellino";
    private static final int SECRET_LENGTH = 20;
    private static final int DIGITS = 6;

    private final TwoFactorAuthQRCodeGeneratorService twoFactorAuthQRCodeGeneratorService;
    private final RecoveryCodeService recoveryCodeService;

    public TwoFactorAuthService(
            TwoFactorAuthQRCodeGeneratorService twoFactorAuthQRCodeGeneratorService,
            RecoveryCodeService recoveryCodeService
    ) {
        this.twoFactorAuthQRCodeGeneratorService = twoFactorAuthQRCodeGeneratorService;
        this.recoveryCodeService = recoveryCodeService;
    }


    /**
     * This method sets up two-factor authentication by generating a secret key and URL that can be used to set up a Two-Factor Authentication (2FA) app.
     *
     * @param email The email of the user who wants to set up two-factor authentication.
     * @return A TwoFactorAuthSetupDTO object containing the secret key and URL for setting up 2FA.
     */
    public TwoFactorAuthSetupDTO setup(String email) {
        String secret = OTP.randomBase32(TwoFactorAuthService.SECRET_LENGTH);
        String url = OTP.getURL(secret, TwoFactorAuthService.DIGITS, Type.TOTP, TwoFactorAuthService.LABEL, email);
        return new TwoFactorAuthSetupDTO(secret, TwoFactorAuthService.LABEL, url);
    }

    /**
     * Injects a QR code into the two-factor authentication setup DTO. The generated image is then encoded and included in the returned DTO.
     *
     * @param twoFactorAuthSetupDTO A TwoFactorAuthSetupDTO object containing the secret key and label required for generating the QR code.
     * @return A TwoFactorAuthSetupWithQRCodeDTO object containing both the original setup data and a Base64-encoded image of the generated QR code.
     */
    public TwoFactorAuthSetupWithQRCodeDTO injectQRCode(TwoFactorAuthSetupDTO twoFactorAuthSetupDTO) {
        String QRCodeImage = this.twoFactorAuthQRCodeGeneratorService.generateQRCode(twoFactorAuthSetupDTO);
        return TwoFactorAuthSetupWithQRCodeDTO.fromTwoFactorAuthSetupDTO(twoFactorAuthSetupDTO, QRCodeImage);
    }

    /**
     * Enables two-factor authentication for a user by generating a set of recovery codes.
     *
     * @param userId the ID of the user for whom two-factor authentication is being enabled.
     * @return a TwoFactorAuthRecoveryCodeCollectionDTO containing an array of generated recovery codes in plain text format.
     */
    public TwoFactorAuthRecoveryCodeCollectionDTO enable(int userId) {
        return this.generateRecoveryCodes(userId);
    }

    /**
     * Verifies a given authentication code against a secret using the TOTP algorithm.
     *
     * @param secret The secret key used to validate the code.
     * @param code The authentication code to verify.
     * @return true if the code is valid for the given secret; false otherwise.
     */
    public boolean verify(String secret, String code) {
        try {
            String hexTime = OTP.timeInHex(System.currentTimeMillis(), 30);

            String textCode = OTP.create(secret, hexTime, 6, Type.TOTP);
            System.out.println(OTP.verify(secret, hexTime, code, TwoFactorAuthService.DIGITS, Type.TOTP));
            System.out.println(OTP.verify(secret, hexTime, textCode, TwoFactorAuthService.DIGITS, Type.TOTP));
            System.out.println(textCode);

            return OTP.verify(secret, hexTime, code, TwoFactorAuthService.DIGITS, Type.TOTP);
        } catch (NoSuchAlgorithmException | InvalidKeyException ignored) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks whether the provided two-factor authentication code is valid for the given secret.
     *
     * @param secret The secret key used to verify the authentication code.
     * @param code The two-factor authentication code to be verified.
     * @throws NotInitializedTwoFactorAuthException If the secret is null or blank.
     * @throws InvalidCodeTwoFactorAuthException If the authentication code is invalid for the given secret.
     */
    public void check(String secret, String code) {
        if ( secret == null || secret.isBlank() ){
            throw new NotInitializedTwoFactorAuthException("Two-factor authentication has not been initialized yet.");
        }
        if ( !this.verify(secret, code) ){
            throw new InvalidCodeTwoFactorAuthException("Invalid two-factor authentication code.");
        }
    }

    /**
     * Rotates the recovery codes for a user by invalidating all existing codes and generating a new set of recovery codes.
     *
     * @param userId the ID of the user for whom the recovery codes are being rotated.
     * @return a TwoFactorAuthRecoveryCodeCollectionDTO containing the newly generated recovery codes.
     */
    @Transactional
    public TwoFactorAuthRecoveryCodeCollectionDTO rotateRecoveryCodes(int userId) {
        this.recoveryCodeService.invalidateAll(userId);
        return this.generateRecoveryCodes(userId);
    }

    /**
     * Disables two-factor authentication for a user and invalidates all associated recovery codes.
     *
     * @param userId the ID of the user for whom two-factor authentication is being disabled.
     */
    public void disable(int userId) {
        this.recoveryCodeService.invalidateAll(userId);
    }

    /**
     * Checks the validity of the provided two-factor authentication code and disables
     * two-factor authentication for the specified user if the code is valid.
     *
     * @param userId The ID of the user for whom two-factor authentication will be disabled.
     * @param secret The secret key used to verify the authentication code.
     * @param code The two-factor authentication code to be verified.
     * @throws NotInitializedTwoFactorAuthException If the secret is null or blank.
     * @throws InvalidCodeTwoFactorAuthException If the authentication code is invalid for the given secret.
     */
    public void checkAndDisable(int userId, String secret, String code) {
        this.check(secret, code);
        this.disable(userId);
    }

    /**
     * Validates the provided two-factor authentication code and enables two-factor authentication for the specified user.
     *
     * @param userId The ID of the user for whom two-factor authentication will be enabled.
     * @param secret The secret key used to verify the authentication code.
     * @param code The two-factor authentication code to be verified.
     * @return a TwoFactorAuthConfigurationDTO containing an array of generated recovery codes in plain text format.
     * @throws NotInitializedTwoFactorAuthException If the secret is null or blank.
     * @throws InvalidCodeTwoFactorAuthException If the authentication code is invalid for the given secret.
     */
    public TwoFactorAuthRecoveryCodeCollectionDTO checkAndEnable(int userId, String secret, String code) {
        this.check(secret, code);
        return this.enable(userId);
    }

    /**
     * Generates a set of recovery codes for two-factor authentication for a specified user.
     *
     * @param userId the ID of the user for whom the recovery codes are being generated.
     * @return a TwoFactorAuthRecoveryCodeCollectionDTO containing an array of recovery codes in plain text format.
     */
    protected TwoFactorAuthRecoveryCodeCollectionDTO generateRecoveryCodes(int userId) {
        List<RecoveryCode> recoveryCodeList = this.recoveryCodeService.generateMulti(userId, TwoFactorAuthService.RECOVERY_CODE_AMOUNT);
        String[] codeList = recoveryCodeList.stream().map(RecoveryCode::getPlainTextCode).toArray(String[]::new);
        return new TwoFactorAuthRecoveryCodeCollectionDTO(codeList);
    }
}
