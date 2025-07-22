package dev.enricosola.porcellino.notifications.user;

import dev.enricosola.porcellino.support.email.EmailEnvelope;
import dev.enricosola.porcellino.support.email.TemplateEngine;
import dev.enricosola.porcellino.entity.User;
import java.util.Map;

public class PreviousEmailChangeUserEmailNotification extends UserEmailNotification {
    private static final String EMAIL_TEMPLATE_PATH = "mails/user/email_change.html";
    private static final String EMAIL_SUBJECT = "Your email address has been changed";

    private final String previousEmailAddress;

    private Map<String, String> buildVariables() {
        return Map.of(
                "previousEmailAddress", this.previousEmailAddress,
                "emailAddress", this.user.getEmail()
        );
    }

    public PreviousEmailChangeUserEmailNotification(User user, String previousEmailAddress) {
        super(user);

        this.previousEmailAddress = previousEmailAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailEnvelope getEmailEnvelope() {
        return new EmailEnvelope(this.previousEmailAddress, PreviousEmailChangeUserEmailNotification.EMAIL_SUBJECT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlainTextBody() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHTMLBody() {
        return new TemplateEngine().render(PreviousEmailChangeUserEmailNotification.EMAIL_TEMPLATE_PATH, this.buildVariables());
    }
}
