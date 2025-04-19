package dev.enricosola.porcellino.service;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;

@Service
@Slf4j
public class VerificationTokenService {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generate(String identifier, Date expiration) {
        SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject(identifier).expiration(expiration).issuedAt(new Date()).signWith(secretKey).compact();
    }

    public boolean validate(String identifier, String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            String tokenIdentifier = jwtParser.parseSignedClaims(token).getPayload().getSubject();
            return MessageDigest.isEqual(identifier.getBytes(StandardCharsets.UTF_8), tokenIdentifier.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ignored) {
            return false;
        }
    }
}
