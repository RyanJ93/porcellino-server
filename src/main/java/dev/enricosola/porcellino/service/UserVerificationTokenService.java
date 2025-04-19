package dev.enricosola.porcellino.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import java.util.Date;

@Service
public class UserVerificationTokenService {
    private final VerificationTokenService verificationTokenService;

    @Value("${app.userVerificationTokenTTL:86400}")
    private int tokenTTL;

    public UserVerificationTokenService(VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }

    /**
     * Generate a new verification token for a given user.
     *
     * @param user The user to verify.
     *
     * @return The generated verification token.
     */
    public String generate(User user) {
        Date expiration = new Date(System.currentTimeMillis() + (this.tokenTTL * 1000L));
        return this.verificationTokenService.generate(String.valueOf(user.getId()), expiration);
    }

    /**
     * Verify a given user using a given verification token.
     *
     * @param user The user to verify.
     * @param token The verification token.
     *
     * @return True if given verification token matches.
     */
    public boolean validate(User user, String token) {
        return this.verificationTokenService.validate(String.valueOf(user.getId()), token);
    }
}
