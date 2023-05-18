package tfip.akimori.server.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Email sent to: " + toEmail);
    }

    public void sendEmailWithAttachment(String toEmail, String subject, String body, String attachment)
            throws MessagingException {
        MimeMessage mimeMsg = mailSender.createMimeMessage();
        MimeMessageHelper mimeMsgHelper = new MimeMessageHelper(mimeMsg, true);
        mimeMsgHelper.setFrom(fromEmail);
        mimeMsgHelper.setTo(toEmail);
        mimeMsgHelper.setSubject(subject);
        mimeMsgHelper.setText(body);

        FileSystemResource fsr = new FileSystemResource(new File(attachment));
        mimeMsgHelper.addAttachment(fsr.getFilename(), fsr);

        mailSender.send(mimeMsg);
        System.out.println("Email sent to: " + toEmail);
    }
}
