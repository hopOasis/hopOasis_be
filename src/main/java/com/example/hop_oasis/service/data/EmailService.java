package com.example.hop_oasis.service.data;


import com.example.hop_oasis.utils.EmailPattern;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {
    @Value("${sendgrid.api.key}")
    private final String sendGridApiKey;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            if (sendGridApiKey.isBlank()) {
                log.error("SendGrid API Key is missing! Email will not be sent.");
                return;
            }

            Email from = new Email(EmailPattern.EMAIL_SENDER);
            Email to = new Email(toEmail);
            Content content = new Content("text/plain", body);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sendGrid = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint(EmailPattern.ENDPOINT);
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            log.info("Email sent! Status code: {}", response.getStatusCode());

        } catch (IOException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }
}


