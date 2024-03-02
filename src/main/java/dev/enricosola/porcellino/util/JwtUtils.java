package dev.enricosola.porcellino.util;

import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import javax.crypto.SecretKey;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generateJwtToken(Authentication authentication){
        AuthenticatedUserDetails authenticatedUserDetails = (AuthenticatedUserDetails) authentication.getPrincipal();
        SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
        JwtBuilder jwtBuilder = Jwts.builder().subject((authenticatedUserDetails.getUsername()));
        jwtBuilder.expiration(new Date((new Date()).getTime() + this.jwtExpirationMs));
        return jwtBuilder.issuedAt(new Date()).signWith(secretKey).compact();
    }

    public String getUserNameFromJwtToken(String token){
        SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        return jwtParser.parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(authToken);
            return true;
        }catch(MalformedJwtException ex){
            JwtUtils.logger.error("Invalid JWT token: {}", ex.getMessage());
        }catch(ExpiredJwtException ex){
            JwtUtils.logger.error("JWT token is expired: {}", ex.getMessage());
        }catch(UnsupportedJwtException ex){
            JwtUtils.logger.error("JWT token is unsupported: {}", ex.getMessage());
        }catch(IllegalArgumentException ex){
            JwtUtils.logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
}
