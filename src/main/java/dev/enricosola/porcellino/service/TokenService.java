package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.auth.token.GenerationTokenException;
import dev.enricosola.porcellino.exception.auth.token.InvalidTokenException;
import dev.enricosola.porcellino.exception.auth.token.UnpackTokenException;
import dev.enricosola.porcellino.dto.UserTokenDTO;
import dev.enricosola.porcellino.entity.User;
import java.util.Map;

public interface TokenService {
    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @return The generated token.
     * @throws GenerationTokenException If an error occurs during token generation.
     */
    UserTokenDTO generate(User user);

    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @return The generated token.
     * @throws GenerationTokenException If an error occurs during token generation.
     */
    UserTokenDTO generate(User user, String[] scopes);

    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @param payload An optional custom payload to add to the generated token.
     * @return The generated token.
     * @throws GenerationTokenException If an error occurs during token generation.
     */
    UserTokenDTO generate(User user, String[] scopes, Map<String, String> payload);

    /**
     * Unpacks the given token and retrieves the associated user information.
     *
     * @param token The token to unpack.
     * @return The unpacked token details, including user information, token, and payload.
     * @throws UnpackTokenException If an error occurs while unpacking the token.
     * @throws InvalidTokenException If the token is invalid.
     */
    UserTokenDTO unpack(String token);

    /**
     * Verifies the validity of the provided token.
     *
     * @param token The token to be verified.
     * @return true if the token is valid; false otherwise.
     */
    boolean verify(String token);
}
