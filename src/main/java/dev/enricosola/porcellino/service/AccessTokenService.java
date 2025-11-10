package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.auth.token.GenerationTokenException;
import dev.enricosola.porcellino.exception.auth.token.InvalidTokenException;
import dev.enricosola.porcellino.exception.auth.token.UnpackTokenException;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.enricosola.porcellino.dto.UserTokenDTO;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtParser;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccessTokenService implements TokenService {
    private static class AccessTokenBody {
        public Map<String, String> payload;
        public String[] scopes;
        public int userId;
    }

    private final UserLookupService userLookupService;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.accessTokenTTL:3600}")
    private int tokenTTL;

    /**
     * Generate a new JWT access token.
     *
     * @param user The user the generated token is associated with.
     * @return The generated token.
     * @throws GenerationTokenException If an error occurs during token generation.
     */
    public UserTokenDTO generate(User user) {
        return this.generate(user, new String[]{}, new HashMap<>());
    }

    /**
     * Generate a new JWT access token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @return The generated token.
     * @throws GenerationTokenException If an error occurs during token generation.
     */
    public UserTokenDTO generate(User user, String[] scopes) {
        return this.generate(user, scopes, new HashMap<>());
    }

    /**
     * Generate a new JWT access token.
     *
     * @param user The user the generated token is associated with.
     * @param scopes Some scopes associated with this token.
     * @param payload An optional custom payload to add to the generated token.
     * @return The generated token.
     * @throws GenerationTokenException If an error occurs during token generation.
     */
    public UserTokenDTO generate(User user, String[] scopes, Map<String, String> payload) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + (this.tokenTTL * 1000L));
            String token = Jwts.builder()
                    .signWith(Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .subject(this.buildBody(user, scopes, payload))
                    .expiration(expiration)
                    .issuedAt(new Date())
                    .compact();
            return UserTokenDTO.builder()
                    .expiration(expiration)
                    .payload(payload)
                    .scopes(scopes)
                    .token(token)
                    .user(user)
                    .build();
        } catch (JsonProcessingException ex) {
            throw new GenerationTokenException("Unable to generate token body", ex);
        }
    }

    /**
     * Unpacks the given token and retrieves the associated user information.
     *
     * @param token The token to unpack.
     * @return The unpacked token details, including user information, token, and payload.
     * @throws UnpackTokenException If an error occurs while unpacking the token.
     * @throws InvalidTokenException If the token is invalid.
     */
    public UserTokenDTO unpack(String token) {
        if ( !this.verify(token) ){
            throw new InvalidTokenException("Token is invalid.");
        }
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            var payload = jwtParser.parseSignedClaims(token).getPayload();
            AccessTokenBody accessTokenBody = new ObjectMapper().readValue(payload.getSubject(), AccessTokenBody.class);
            User user = this.userLookupService.find(accessTokenBody.userId);
            return UserTokenDTO.builder()
                    .expiration(payload.getExpiration())
                    .payload(accessTokenBody.payload)
                    .scopes(accessTokenBody.scopes)
                    .token(token)
                    .user(user)
                    .build();
        } catch (JsonProcessingException ex) {
            throw new UnpackTokenException("Unable to unpack token body", ex);
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
            SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Builds and serializes the access token body as a JSON string.
     *
     * @param user The user associated with the token being built.
     * @param scopes An array of scopes to include in the token.
     * @param payload A map representing additional custom key-value pairs to include in the token's payload.
     * @return A JSON string representing the serialized access token body.
     * @throws JsonProcessingException If an error occurs during JSON serialization.
     */
    private String buildBody(User user, String[] scopes, Map<String, String> payload) throws JsonProcessingException {
        AccessTokenBody accessTokenBody = new AccessTokenBody();
        accessTokenBody.userId = user.getId();
        accessTokenBody.payload = payload;
        accessTokenBody.scopes = scopes;
        return new ObjectMapper().writeValueAsString(accessTokenBody);
    }
}
