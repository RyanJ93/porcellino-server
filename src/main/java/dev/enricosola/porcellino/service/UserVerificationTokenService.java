package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.MalformedVerificationTokenException;
import dev.enricosola.porcellino.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import java.util.Date;

@Service
public class UserVerificationTokenService {
    protected static final String VERIFICATION_TOKEN_SCOPE = "user_verification";

    protected final VerificationTokenService verificationTokenService;
    protected final UserLookupService userLookupService;

    @Value("${app.userVerificationTokenTTL:86400}")
    protected int tokenTTL;

    public UserVerificationTokenService(
            VerificationTokenService verificationTokenService,
            UserLookupService userLookupService
    ) {
        this.verificationTokenService = verificationTokenService;
        this.userLookupService = userLookupService;
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
        return this.verificationTokenService.generate(
                String.valueOf(user.getId()),
                UserVerificationTokenService.VERIFICATION_TOKEN_SCOPE,
                expiration
        );
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
        return this.verificationTokenService.validate(
                String.valueOf(user.getId()),
                UserVerificationTokenService.VERIFICATION_TOKEN_SCOPE,
                token
        );
    }

    /**
     * Extract the user from a given verification token.
     *
     * @param token The JWT verification token the user should be extracted from.
     *
     * @return The extracted user.
     *
     * @throws MalformedVerificationTokenException If the given token is malformed or does not contain the required components.
     * @throws NotFoundException If no user matching extracted ID is found.
     */
    public User extractUserFromToken(String token) {
        String identifier = this.verificationTokenService.extractIdentifierFromToken(token);
        return this.userLookupService.find(Integer.parseInt(identifier));
    }
}
