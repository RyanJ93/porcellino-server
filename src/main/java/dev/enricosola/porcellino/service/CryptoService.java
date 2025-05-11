package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.DecryptException;
import dev.enricosola.porcellino.exception.EncryptException;

public interface CryptoService {
    /**
     * Encrypts the provided plain text.
     *
     * @param plainText The plain text to be encrypted.
     * @return The encrypted representation of the provided plain text.
     */
    String encrypt(String plainText) throws EncryptException;

    /**
     * Decrypts the provided encrypted text.
     *
     * @param encryptedText The encrypted text to be decrypted.
     * @return The decrypted textual representation of the input.
     */
    String decrypt(String encryptedText) throws DecryptException;
}
