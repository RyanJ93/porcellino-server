package dev.enricosola.porcellino.notifications;

import dev.enricosola.porcellino.support.email.EmailEnvelope;

public abstract class EmailNotification extends Notification {
    /**
     * Generate the email envelope containing both the recipient and the message subject.
     */
    public abstract EmailEnvelope getEmailEnvelope();

    /**
     * Return the email message as a plain text.
     */
    public abstract String getPlainTextBody();

    /**
     * Return the email message body.
     */
    public abstract String getHTMLBody();
}
