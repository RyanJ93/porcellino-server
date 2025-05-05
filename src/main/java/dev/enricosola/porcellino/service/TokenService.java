package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.TokenGenerationException;
import dev.enricosola.porcellino.exception.InvalidTokenException;
import dev.enricosola.porcellino.exception.TokenUnpackException;
import dev.enricosola.porcellino.dto.UserTokenDTO;
import dev.enricosola.porcellino.entity.User;
import java.util.Map;

public interface TokenService {
    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @return The generated token.
     * @throws TokenGenerationException If an error occurs during token generation.
     */
    UserTokenDTO generate(User user) throws TokenGenerationException;

    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @return The generated token.
     * @throws TokenGenerationException If an error occurs during token generation.
     */
    UserTokenDTO generate(User user, String[] scopes) throws TokenGenerationException;

    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @param payload An optional custom payload to add to the generated token.
     * @return The generated token.
     * @throws TokenGenerationException If an error occurs during token generation.
     */
    UserTokenDTO generate(User user, String[] scopes, Map<String, String> payload) throws TokenGenerationException;

    /**
     * Unpacks the given token and retrieves the associated user information.
     *
     * @param token The token to unpack.
     * @return The unpacked token details, including user information, token, and payload.
     * @throws TokenUnpackException If an error occurs while unpacking the token.
     */
    UserTokenDTO unpack(String token) throws TokenUnpackException, InvalidTokenException;

    /**
     * Verifies the validity of the provided token.
     *
     * @param token The token to be verified.
     * @return true if the token is valid; false otherwise.
     */
    boolean verify(String token);
}
