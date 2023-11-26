package com.example.krafs1;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends AsyncTask<Void, Void, Void> {
    private final String username = "krafsteam@gmail.com";
    private final String password = "krafs_team12345";
    private String recipientEmail;
    private String subject;
    private String message;

    public SendMail(String recipientEmail, String subject, String message) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            Log.e("SendMail", "Error: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
