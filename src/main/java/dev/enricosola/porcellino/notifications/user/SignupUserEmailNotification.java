package dev.enricosola.porcellino.notifications.user;

import dev.enricosola.porcellino.support.email.TemplateEngine;
import dev.enricosola.porcellino.support.email.EmailEnvelope;
import dev.enricosola.porcellino.entity.User;
import dev.enricosola.porcellino.facades.App;
import java.util.Map;

public class SignupUserEmailNotification extends UserEmailNotification {
    private static final String EMAIL_TEMPLATE_PATH = "mails/user/signup.html";
    private static final String EMAIL_SUBJECT = "Welcome to Porcellino!";

    private final String verificationToken;

    private Map<String, String> buildVariables() {
        String verificationURL = App.getUrl() + "/verification?token=" + this.verificationToken;
        return Map.of("verificationURL", verificationURL);
    }

    /**
     * The class constructor.
     *
     * @param user The user this notification will be delivered to.
     */
    public SignupUserEmailNotification(User user, String verificationToken) {
        super(user);

        this.verificationToken = verificationToken;
    }

    /**
     * Generate the email envelope for the defined user.
     */
    public EmailEnvelope getEmailEnvelope() {
        return new EmailEnvelope(this.user.getEmail(), SignupUserEmailNotification.EMAIL_SUBJECT);
    }

    /**
     * Return the email message as a plain text (not available for this notification).
     */
    public String getPlainTextBody() {
        return null;
    }

    /**
     * Return the email message body.
     */
    public String getHTMLBody() {
        return new TemplateEngine().render(SignupUserEmailNotification.EMAIL_TEMPLATE_PATH, this.buildVariables());
    }
}
