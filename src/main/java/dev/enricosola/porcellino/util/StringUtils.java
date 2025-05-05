package dev.enricosola.porcellino.util;

import java.security.SecureRandom;

public class StringUtils {
    private static final String PATTERN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Generates a cryptographically secure random string with the specified length.
     *
     * @param length the desired length of the generated string.
     * @return a randomly generated string containing alphanumeric characters.
     */
    public static String generateCryptoRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        SecureRandom secureRandom = new SecureRandom();
        for ( int i = 0 ; i < length ; i++ ) {
            int randomIndex = secureRandom.nextInt(StringUtils.PATTERN.length());
            stringBuilder.append(StringUtils.PATTERN.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}
