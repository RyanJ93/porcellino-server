package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.user.MalformedVerificationTokenUserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;
import java.security.MessageDigest;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.JwtParser;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import java.util.Date;

@Service
@Slf4j
public class VerificationTokenService {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * Generate a new JWT verification token.
     *
     * @param identifier The identifier to be verified later on.
     * @param scope The scope of the verification token.
     * @param expiration The expiration date of the verification token.
     *
     * @return The generated JWT token.
     */
    public String generate(String identifier, String scope, Date expiration) {
        SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
        String subject = String.format("%s:%s", scope, identifier);
        return Jwts.builder().subject(subject).expiration(expiration).issuedAt(new Date()).signWith(secretKey).compact();
    }

    /**
     * Validate a given verification token.
     *
     * @param identifier The identifier to be verified.
     * @param scope The scope of the verification token.
     * @param token The verification token to validate.
     *
     * @return True if given verification token matches.
     */
    public boolean validate(String identifier, String scope, String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            String[] components = jwtParser.parseSignedClaims(token).getPayload().getSubject().split(":");
            if ( components.length != 2 || !components[0].equals(scope) ){
                return false;
            }
            byte[] tokenIdentifier = components[1].getBytes(StandardCharsets.UTF_8);
            return MessageDigest.isEqual(identifier.getBytes(StandardCharsets.UTF_8), tokenIdentifier);
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Extract the identifier from a given verification token.
     *
     * @param token The JWT token the identifier should be extracted from.
     *
     * @return The extracted identifier.
     *
     * @throws MalformedVerificationTokenUserException If the given token is malformed or does not contain the required components.
     */
    public String extractIdentifierFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        String[] components = jwtParser.parseSignedClaims(token).getPayload().getSubject().split(":");
        if ( components.length != 2 ){
            throw new MalformedVerificationTokenUserException("JWT token misses some required components in its payload.");
        }
        return components[1];
    }
}
