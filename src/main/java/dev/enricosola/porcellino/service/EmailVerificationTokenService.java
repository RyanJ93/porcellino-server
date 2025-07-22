package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.InvalidPendingEmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

@Service
@Slf4j
public class EmailVerificationTokenService {
    protected static final String VERIFICATION_TOKEN_SCOPE = "email_verification";

    protected final VerificationTokenService verificationTokenService;
    protected final UserLookupService userLookupService;

    @Value("${app.emailVerificationTokenTTL:86400}")
    protected int tokenTTL;

    public EmailVerificationTokenService(
            VerificationTokenService verificationTokenService,
            UserLookupService userLookupService
    ) {
        this.verificationTokenService = verificationTokenService;
        this.userLookupService = userLookupService;
    }

    /**
     * Generates a new email verification token for the provided user.
     *
     * @param user The user for whom the email verification token is to be generated. The user must have a valid pending email address.
     * @return The generated email verification token as a string.
     * @throws InvalidPendingEmailException If the user does not have a valid pending email address.
     */
    public String generate(User user) {
        if ( user.getPendingEmail() == null || user.getPendingEmail().isBlank() ) {
            throw new InvalidPendingEmailException("User does not have a pending email address.");
        }
        Date expire = new Date(System.currentTimeMillis() + (this.tokenTTL * 1000L));
        return this.verificationTokenService.generate(user.getPendingEmail(), VERIFICATION_TOKEN_SCOPE, expire);
    }

    /**
     * Validates a provided verification token for the user's pending email address.
     *
     * @param user The user for whom the email verification is being validated. The user must have a valid pending email.
     * @param token The verification token to validate against the user's pending email.
     * @return True if the verification token is valid for the user's pending email, otherwise false.
     * @throws InvalidPendingEmailException If the user does not have a pending email address.
     */
    public boolean validate(User user, String token) {
        if ( user.getPendingEmail() == null || user.getPendingEmail().isBlank() ) {
            throw new InvalidPendingEmailException("User does not have a pending email address.");
        }
        return this.verificationTokenService.validate(user.getPendingEmail(), VERIFICATION_TOKEN_SCOPE, token);
    }

    /**
     * Extracts the pending email address encoded in a verification token.
     *
     * @param token The verification token from which the pending email address is to be extracted.
     * @return The pending email address encoded in the verification token.
     */
    public String extractPendingEmailFromToken(String token) {
        return this.verificationTokenService.extractIdentifierFromToken(token);
    }
}
