package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.repository.RefreshTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import dev.enricosola.porcellino.entity.RefreshToken;
import dev.enricosola.porcellino.dto.ClientInfoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.enricosola.porcellino.dto.UserTokenDTO;
import dev.enricosola.porcellino.util.StringUtils;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import dev.enricosola.porcellino.exception.*;
import java.util.*;

@Service
public class RefreshTokenService implements TokenService {
    private static final String CLIENT_TRACKING_REF_NAME = "refresh_token";

    private final RefreshTokenRepository refreshTokenRepository;
    private final ClientTrackingService clientTrackingService;
    private final UserLookupService userLookupService;

    @Value("${app.refreshTokenLength:1024}")
    private int refreshTokenLength;

    @Value("${app.refreshTokenTTL:1296000}")
    private int refreshTokenTTL;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            ClientTrackingService clientTrackingService,
            UserLookupService userLookupService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.clientTrackingService = clientTrackingService;
        this.userLookupService = userLookupService;
    }

    /**
     * Retrieves a list of all refresh tokens associated with a specific user ID.
     *
     * @param userId the ID of the user whose refresh tokens are to be retrieved
     * @return a list of {@code RefreshToken} objects associated with the specified user ID.
     */
    public List<RefreshToken> findAllByUserId(int userId) {
        return this.refreshTokenRepository.findAllByUserId(userId);
    }

    /**
     * Finds a refresh token by its token string.
     *
     * @param token The token string to search for.
     * @return The refresh token associated with the given token string.
     * @throws NotFoundException If no refresh token is found for the given token string.
     */
    public RefreshToken find(String token) {
        Optional<RefreshToken> refreshToken = this.refreshTokenRepository.findByToken(token);
        return refreshToken.orElseThrow(() -> new NotFoundException("No such token found."));
    }

    /**
     * Finds a refresh token by its token string and validates its expiration date.
     *
     * @param token The token string to search for and validate.
     * @return The refresh token if it is found and not expired.
     * @throws NotFoundException If no refresh token is found for the given token string.
     * @throws RefreshTokenExpiredException If the refresh token has already expired.
     */
    public RefreshToken findAndValidate(String token) {
        RefreshToken refreshToken = this.find(token);
        if ( refreshToken.getExpiredAt().before(new Date()) ) {
            throw new RefreshTokenExpiredException("Refresh token has expired.");
        }
        return refreshToken;
    }

    /**
     * Finds a refresh token by its token string, validates it, and rotates it by generating a new token and extending its expiration date.
     *
     * @param token The token string to be found, validated, and rotated.
     * @return The updated refresh token with a new token string and updated expiration time.
     * @throws NotFoundException If no refresh token is found for the given token string.
     * @throws RefreshTokenExpiredException If the refresh token has already expired.
     */
    public RefreshToken findAndRotate(String token) {
        Date expiration = new Date(System.currentTimeMillis() + (this.refreshTokenTTL * 1000L));
        String newToken = StringUtils.generateCryptoRandomString(this.refreshTokenLength);
        RefreshToken refreshToken = this.findAndValidate(token);
        refreshToken.setExpiredAt(expiration);
        refreshToken.setToken(newToken);
        return this.refreshTokenRepository.save(refreshToken);
    }

    /**
     * Finds a refresh token by its token string and deletes it from the repository.
     *
     * @param token The token string used to locate and delete the refresh token.
     * @throws NotFoundException If no refresh token is found for the given token string.
     */
    @Transactional
    public void findAndDelete(String token) {
        RefreshToken refreshToken = this.find(token);
        try {
            this.clientTrackingService.findByRefAndDelete(RefreshTokenService.CLIENT_TRACKING_REF_NAME, refreshToken.getId());
        } catch (NotFoundException ignored) {}
        this.refreshTokenRepository.delete(refreshToken);
    }

    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @return The generated token.
     * @throws TokenGenerationException If an error occurs during token generation.
     */
    public UserTokenDTO generate(User user) throws TokenGenerationException {
        return this.generate(user, new String[]{}, new HashMap<>(), null);
    }

    /**
     * Generate a new token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @return The generated token.
     * @throws TokenGenerationException If an error occurs during token generation.
     */
    public UserTokenDTO generate(User user, String[] scopes) throws TokenGenerationException {
        return this.generate(user, scopes, new HashMap<>(), null);
    }

    /**
     * Generate a new refresh token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @param payload An optional custom payload to add to the generated token.
     * @return The generated token.
     * @throws TokenGenerationException If an error occurs during token generation.
     */
    public UserTokenDTO generate(User user, String[] scopes, Map<String, String> payload) throws TokenGenerationException {
        return this.generate(user, scopes, payload, null);
    }

    /**
     * GGenerate a new refresh token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes The array of scopes associated with the token.
     * @param payload A custom key-value data payload that will be included in the token.
     * @param clientInfoDTO Information about the client (e.g., user agent and IP address).
     * @return A UserTokenDTO containing details of the generated token, user, scopes, and payload.
     * @throws TokenGenerationException If an error occurs during the token generation process.
     */
    public UserTokenDTO generate(User user, String[] scopes, Map<String, String> payload, ClientInfoDTO clientInfoDTO) throws TokenGenerationException {
         try {
             Date expiration = new Date(System.currentTimeMillis() + (this.refreshTokenTTL * 1000L));
             String token = StringUtils.generateCryptoRandomString(this.refreshTokenLength);
             RefreshToken refreshToken = new RefreshToken();
             refreshToken.setPayload(new ObjectMapper().writeValueAsString(payload));
             refreshToken.setScopes(String.join(",", scopes));
             refreshToken.setExpiredAt(expiration);
             refreshToken.setToken(token);
             refreshToken.setUser(user);
             this.refreshTokenRepository.save(refreshToken);
             if ( clientInfoDTO != null ) {
                 this.clientTrackingService.track(RefreshTokenService.CLIENT_TRACKING_REF_NAME, refreshToken.getId(), clientInfoDTO);
             }
             return UserTokenDTO.builder()
                     .expiration(expiration)
                     .payload(payload)
                     .scopes(scopes)
                     .token(token)
                     .user(user)
                     .build();
         } catch (JsonProcessingException ex) {
             throw new TokenGenerationException("Unable to serialize payload to JSON.", ex);
         }
    }

    /**
     * Unpacks the given token and retrieves the associated user information.
     *
     * @param token The token to unpack.
     * @return The unpacked token details, including user information, token, and payload.
     * @throws TokenUnpackException If an error occurs while unpacking the token.
     */
    public UserTokenDTO unpack(String token) throws TokenUnpackException, InvalidTokenException {
        try {
            RefreshToken refreshToken = this.findAndValidate(token);
            HashMap<String, String> payload = new HashMap<>();
            String[] scopes = {};
            User user = this.userLookupService.find(refreshToken.getUser().getId());
            if ( refreshToken.getPayload() != null && !refreshToken.getPayload().isBlank() ) {
                payload = new ObjectMapper().readValue(refreshToken.getPayload(), HashMap.class);
            }
            if ( refreshToken.getScopes() != null && !refreshToken.getScopes().isBlank() ) {
                scopes = refreshToken.getScopes().split(",");
            }
            return UserTokenDTO.builder()
                    .expiration(refreshToken.getExpiredAt())
                    .payload(payload)
                    .scopes(scopes)
                    .token(token)
                    .user(user)
                    .build();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Unable to deserialize payload from JSON.", ex);
        }
    }

    /**
     * Verifies the validity of the provided token.
     *
     * @param token The token to be verified.
     * @return true if the token is valid; false otherwise.
     */
    public boolean verify(String token) {
        try {
            this.findAndValidate(token);
            return true;
        } catch (InvalidTokenException ignored) {
            return false;
        }
    }
}
