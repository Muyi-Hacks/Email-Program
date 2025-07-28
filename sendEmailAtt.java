/*
 * Name: Muyi Omorogbe
 * Date: 06/19/25
 * Assignment #4 - Question #3
 *
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person
 * has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any
 * academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 *
 * Program Design:
* - Takes server, user, password as arguments
 * - Sets up IMAP over SSL (imaps)
 * - Connects to the inbox
 * - Searches for unread messages
 * - If no message number: lists unread emails with subject and sender
 * - If message number is given: prints that email’s subject, sender, and plain text content
 * - Handles both plain and multipart emails
 * -Handles attachment
 * - Closes inbox and disconnects cleanly
 *
 * Code Sources:
 * - Code mainly inspired and adapted from: 
 * https://www.youtube.com/watch?v=RayFXivOMvA
 * https://github.com/BornToGeek1/youtube/tree/master/java/gmail-sender
 * https://github.com/RafaMartinezz/MailSender
 * I did not copy any of these people but rather used them as tutorials to guide me through the process
 * 
 * Test Plan:
  * Testing for valid credentials and server
 * Testing for missing required arguments
 * Testing with unread emails in inbox
 * Testing when inbox has no unread emails
 * Testing with valid message number input
 * Trsting with out-of-range message number
 * Testing with plain text emails
 * Testing for if attachments were sent properly
 *
 * Example Usage:
 *   mvn clean compile exec:java -Dexec.mainClass="assignment4.email_programs.sendEmailAtt" -Dexec.args="thisfile.txt"
 *   Output → In email is sent (i hope lol)
 */
//not many comments because the code funtions almost exact like question 1 apart from attachments
package assignment4.email_programs;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class sendEmailAtt {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java sendEmailAtt <email file>");
            return;
        }

        Map<String, String> emailFields = new HashMap<>();
        StringBuilder body = new StringBuilder();
        boolean bodyStart = false;

        for (String line : Files.readAllLines(Paths.get(args[0]))) {
            System.out.println("READ LINE: [" + line + "]");
            if (!bodyStart && line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    emailFields.put(parts[0].trim(), parts[1].trim());
                }
            } else {
                bodyStart = true;
                body.append(line).append("\n");
            }
        }

        // debugging
        System.out.println("\nEMAIL FIELDS:");
        for (Map.Entry<String, String> entry : emailFields.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        String[] requiredKeys = {"Server", "User", "Password", "To", "CC", "BCC", "Subject"};
        for (String key : requiredKeys) {
            if (!emailFields.containsKey(key)) {
                System.err.println("Missing required field in input: " + key);
                return;
            }
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", emailFields.get("Server"));
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        emailFields.get("User"), emailFields.get("Password"));
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailFields.get("User")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailFields.get("To")));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailFields.get("CC")));
        message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailFields.get("BCC")));
        message.setSubject(emailFields.get("Subject"));

        Multipart multipart = new MimeMultipart();

        // Text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body.toString());
        multipart.addBodyPart(textPart);

        //attachment added
        if (emailFields.containsKey("Attachment")) {
            String filePath = emailFields.get("Attachment");
            File file = new File(filePath);
            System.out.println("Checking for attachment at: " + file.getAbsolutePath());

            if (!file.exists()) {
                System.err.println("Attachment file not found: " + file.getAbsolutePath());
                return;
            } else {
                System.out.println("Attachment found: " + file.getName()); //awaiting attachmennt
            }

            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(file.getName());
            multipart.addBodyPart(attachmentPart); //awaiting attachment
        }

        message.setContent(multipart);

        Transport.send(message);
        System.out.println("Email with attachment sent successfully.");
    }
}
