package dev.enricosola.porcellino.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserNotificationService {
    private final ResourceLoader resourceLoader;
    private final JavaMailSender mailSender;

    private String loadMessageBody(String model, Optional<HashMap<String, String>> variables) {
        try {
            Resource resource = this.resourceLoader.getResource("classpath:mails/" + model + ".html");
            String contents = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            if ( variables.isPresent() ) {
                for ( Map.Entry<String, String> entry : variables.get().entrySet() ) {
                    contents = contents.replace("{{ " + entry.getKey() + " }}", entry.getValue());
                }
            }
            System.out.println(contents);
            return contents;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserNotificationService(
        ResourceLoader resourceLoader,
        JavaMailSender mailSender
    ) {
        this.resourceLoader = resourceLoader;
        this.mailSender = mailSender;
    }

    public void sendSignupNotification(User user, String verificationToken){
        try {
            HashMap<String, String> variables = new HashMap<>();
            variables.put("verificationToken", verificationToken);
            variables.put("userID", String.valueOf(user.getId()));
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            String htmlBody = this.loadMessageBody("user_signup", Optional.of(variables));
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("TEST");
            mimeMessageHelper.setText(htmlBody, true);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
