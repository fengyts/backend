package com.backend.component.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
@Async
@Slf4j
public class DefaultMailSender implements MailSender {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public boolean sendSingleMail(String[] to, String subject, String content) {
        boolean result = false;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            sender.send(mailMessage);
            result = true;
        } catch (MailException e) {
            log.error("发送邮件失败：" + e);
        }
        return result;
    }

    @Override
    public boolean sendHtmlMail(String[] to, String subject, String html) {
        boolean result = false;
        try {
            MimeMessage mailMessage = sender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

            messageHelper.setFrom(username);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(html, true); // true 表示启动HTML格式的邮件

            sender.send(mailMessage);
            result = true;
        } catch (Exception e) {
            log.error("发送邮件失败：" + e);
        }
        return result;
    }

    @Override
    public boolean sendInlineFileMail(String[] to, String subject, String html, String filePath) {
        boolean result = false;
        MimeMessage mailMessage = sender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setFrom(username);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(html, true); // true 表示启动HTML格式的邮件

            FileSystemResource file = new FileSystemResource(new File(filePath));
            messageHelper.addInline(file.getFilename(), file);

            sender.send(mailMessage);
            result = true;
        } catch (MessagingException e) {
            log.error("发送邮件失败：" + e);
        }
        return result;
    }

    @Override
    public boolean sendAttachedFileMail(String[] to, String subject, String html, String filePath) {
        boolean result = false;
        try {
            MimeMessage mailMessage = sender.createMimeMessage();
            MimeMessageHelper messageHelper = createMimeMessageHelper(mailMessage, to, subject, html);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            messageHelper.addAttachment(file.getFilename(), file);

            sender.send(mailMessage);
            result = true;
        } catch (Exception e) {
            log.error("发送邮件失败：" + e);
            return false;
        }
        return result;
    }

    @Override
    public void sendAttachedUriFileMail(String[] to, String subject, String html, String fileName, String fileUrl) {
        try {
            MimeMessage mailMessage = sender.createMimeMessage();
            MimeMessageHelper messageHelper = createMimeMessageHelper(mailMessage, to, subject, html);

//            InputStream is = HttpRequestUtils.getUrlResourceFile(fileUrl);
            InputStreamSource iss = new UrlResource(fileUrl);
            messageHelper.addAttachment(fileName, iss);
            sender.send(mailMessage);
        } catch (Exception e) {
            log.error("发送邮件失败：" + e);
        }
    }

    private MimeMessageHelper createMimeMessageHelper(MimeMessage mailMessage, String[] to, String subject, String html) throws Exception {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
        messageHelper.setFrom(username);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true); // true 表示启动HTML格式的邮件
        return messageHelper;
    }

}
