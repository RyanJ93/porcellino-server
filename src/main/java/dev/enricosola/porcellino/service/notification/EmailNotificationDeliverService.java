package dev.enricosola.porcellino.service.notification;

import dev.enricosola.porcellino.notifications.EmailNotification;
import dev.enricosola.porcellino.support.email.EmailEnvelope;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;

@Service
public class EmailNotificationDeliverService extends NotificationDeliverService<EmailNotification> {
    private final JavaMailSender mailSender;

    public EmailNotificationDeliverService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void deliver(EmailNotification notification) {
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setText(notification.getHTMLBody(), true);
            EmailEnvelope emailEnvelope = notification.getEmailEnvelope();
            mimeMessageHelper.setSubject(emailEnvelope.getSubject());
            mimeMessageHelper.setTo(emailEnvelope.getRecipient());
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
