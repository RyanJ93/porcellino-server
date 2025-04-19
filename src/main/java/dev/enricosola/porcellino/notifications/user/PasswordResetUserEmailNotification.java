package dev.enricosola.porcellino.notifications.user;

import dev.enricosola.porcellino.support.email.TemplateEngine;
import dev.enricosola.porcellino.support.email.EmailEnvelope;
import dev.enricosola.porcellino.entity.User;
import dev.enricosola.porcellino.facades.App;
import java.util.Map;

public class PasswordResetUserEmailNotification extends UserEmailNotification {
    private static final String EMAIL_TEMPLATE_PATH = "mails/user/password_reset.html";
    private static final String EMAIL_SUBJECT = "Reset your password";

    private final String token;

    /**
     * The class constructor.
     *
     * @param user The user this notification will be delivered to.
     * @param token The token to be used for password reset.
     */
    public PasswordResetUserEmailNotification(User user, String token) {
        super(user);

        this.token = token;
    }

    /**
     * Generate the email envelope containing both the recipient and the message subject.
     */
    @Override
    public EmailEnvelope getEmailEnvelope() {
        return new EmailEnvelope(this.user.getEmail(), PasswordResetUserEmailNotification.EMAIL_SUBJECT);
    }

    /**
     * Return the email message as a plain text (not available for this notification).
     */
    @Override
    public String getPlainTextBody() {
        return null;
    }

    /**
     * Return the email message body.
     */
    @Override
    public String getHTMLBody() {
        String passwordResetLink = App.getUrl() + "/reset-password?token=" + this.token;
        Map<String, String> variables = Map.of("passwordResetLink", passwordResetLink);
        return new TemplateEngine().render(PasswordResetUserEmailNotification.EMAIL_TEMPLATE_PATH, variables);
    }
}
