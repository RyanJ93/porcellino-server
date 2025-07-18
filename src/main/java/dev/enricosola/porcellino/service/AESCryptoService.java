package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.DecryptException;
import dev.enricosola.porcellino.exception.EncryptException;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;

public class AESCryptoService implements CryptoService {
    private static final String ALGORITHM_NAME = "AES";

    @Value("${app.aesSecretKey}")
    private String secretKey;

    /**
     * Encrypts the provided plain text.
     *
     * @param plainText The plain text to be encrypted.
     * @return The encrypted representation of the provided plain text.
     */
    public String encrypt(String plainText) throws EncryptException  {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(), AESCryptoService.ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(AESCryptoService.ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new EncryptException("Error while encrypting the provided text.", ex);
        }
    }

    /**
     * Decrypts the provided encrypted text.
     *
     * @param encryptedText The encrypted text to be decrypted.
     * @return The decrypted textual representation of the input.
     */
    public String decrypt(String encryptedText) throws DecryptException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(), AESCryptoService.ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(AESCryptoService.ALGORITHM_NAME);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(decoded));
        } catch (Exception ex) {
            throw new DecryptException("Error while decrypting the provided text.", ex);
        }
    }
}
