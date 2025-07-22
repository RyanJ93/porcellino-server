package dev.enricosola.porcellino.notifications.user;

import dev.enricosola.porcellino.support.email.TemplateEngine;
import dev.enricosola.porcellino.support.email.EmailEnvelope;
import dev.enricosola.porcellino.facades.App;
import dev.enricosola.porcellino.entity.User;
import java.util.Map;

public class ConfirmEmailChangeUserEmailNotification extends UserEmailNotification {
    private static final String EMAIL_TEMPLATE_PATH = "mails/user/confirm_email_change.html";
    private static final String EMAIL_SUBJECT = "Confirm your new email address";

    private final String verificationToken;

    /**
     * Builds a map of variables needed for rendering the email template.
     *
     * @return a map containing the variable "verificationURL" with the email verification URL.
     */
    private Map<String, String> buildVariables() {
        String verificationURL = App.getUrl() + "/email-verification";
        verificationURL += "?token=" + this.verificationToken;
        return Map.of("verificationURL", verificationURL);
    }

    public ConfirmEmailChangeUserEmailNotification(User user, String verificationToken) {
        super(user);

        this.verificationToken = verificationToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailEnvelope getEmailEnvelope() {
        return new EmailEnvelope(this.user.getPendingEmail(), ConfirmEmailChangeUserEmailNotification.EMAIL_SUBJECT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlainTextBody() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHTMLBody() {
        return new TemplateEngine().render(ConfirmEmailChangeUserEmailNotification.EMAIL_TEMPLATE_PATH, this.buildVariables());
    }
}
