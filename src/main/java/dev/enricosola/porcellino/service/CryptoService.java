package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.crypto.DecryptionFailedCryptoException;
import dev.enricosola.porcellino.exception.crypto.EncryptionFailedCryptoException;

public interface CryptoService {
    /**
     * Encrypts the provided plain text.
     *
     * @param plainText The plain text to be encrypted.
     * @return The encrypted representation of the provided plain text.
     * @throws EncryptionFailedCryptoException If an error occurs during encryption.
     */
    String encrypt(String plainText);

    /**
     * Decrypts the provided encrypted text.
     *
     * @param encryptedText The encrypted text to be decrypted.
     * @return The decrypted textual representation of the input.
     * @throws DecryptionFailedCryptoException If an error occurs during decryption.
     */
    String decrypt(String encryptedText);
}
