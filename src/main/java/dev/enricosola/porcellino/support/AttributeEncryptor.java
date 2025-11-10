package dev.enricosola.porcellino.support;

import dev.enricosola.porcellino.exception.crypto.DecryptionFailedCryptoException;
import dev.enricosola.porcellino.exception.crypto.EncryptionFailedCryptoException;
import dev.enricosola.porcellino.service.CryptoService;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.Converter;

@RequiredArgsConstructor
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {
    private final CryptoService cryptoService;

    /**
     * Converts the provided value into its encrypted form for secure storage in the database.
     *
     * @param value The plain text value to be encrypted.
     * @return The encrypted representation of the provided plain text value.
     * @throws EncryptionFailedCryptoException If an error occurs during encryption.
     */
    @Override
    public String convertToDatabaseColumn(String value) {
        return this.cryptoService.encrypt(value);
    }

    /**
     * Converts the provided encrypted value into its decrypted form for use within the application.
     *
     * @param value The encrypted value retrieved from the database.
     * @return The decrypted plain text value.
     * @throws DecryptionFailedCryptoException If an error occurs during decryption.
     */
    @Override
    public String convertToEntityAttribute(String value) {
        return this.cryptoService.decrypt(value);
    }
}
