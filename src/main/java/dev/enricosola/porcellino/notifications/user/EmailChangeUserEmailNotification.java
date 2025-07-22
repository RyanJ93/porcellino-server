package dev.enricosola.porcellino.notifications.user;

import dev.enricosola.porcellino.support.email.TemplateEngine;
import dev.enricosola.porcellino.support.email.EmailEnvelope;
import dev.enricosola.porcellino.entity.User;
import java.util.Map;

public class EmailChangeUserEmailNotification extends UserEmailNotification {
    private static final String EMAIL_TEMPLATE_PATH = "mails/user/email_change.html";
    private static final String EMAIL_SUBJECT = "Your email address has been changed";

    private Map<String, String> buildVariables() {
        return Map.of("emailAddress", this.user.getEmail());
    }

    public EmailChangeUserEmailNotification(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailEnvelope getEmailEnvelope() {
        return new EmailEnvelope(this.user.getEmail(), EmailChangeUserEmailNotification.EMAIL_SUBJECT);
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
        return new TemplateEngine().render(EmailChangeUserEmailNotification.EMAIL_TEMPLATE_PATH, this.buildVariables());
    }
}
