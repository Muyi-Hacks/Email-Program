/*
 * Name: Muyi Omorogbe
 * Date: 06/18/25
 * Assignment #4 - Question #1
 *
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person
 * has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any
 * academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 *
 * Program Design:
*  Input Parsing:
 *  Read the input file passed as a command-line argument.
 *  Parsing required email fields (Server, User, Password, To, CC, BCC, Subject).
 *  Capture the rest of the content as the email body.
 *  Validation:
 *  Ensure all required fields are present; print an error and exit if any are missing.
 *  SMTP Configuration:
 *  Set up SMTP properties: host, port (587), TLS, and authentication enabled.
 *  Session and Authentication:
 *  Create a mail Session using an Authenticator with the User and Password.
 *  Email Construction:
 *  Build a MimeMessage: set From, To, CC, BCC, Subject, and Body.
 *  Email Sending:
 *  Send the email using Transport.send().
 *  Debugging:
 *  Print parsed fields and debug lines for troubleshooting.
 *
 * Code Sources:
 * - Code mainly inspired and adapted from: 
 * https://www.youtube.com/watch?v=RayFXivOMvA
 * https://github.com/BornToGeek1/youtube/tree/master/java/gmail-sender
 * https://github.com/RafaMartinezz/MailSender
 * I did not copy any of these people but rather used them as tutorials to guide me through the process
 * 
 * Test Plan:
 * Testing for valid input
 * Test for missing required fields
 * test for correct SMTP credentials
 * test for valid input files
 * Testing for mulltiple CC and BCC recipients
 *
 * Example Usage:
 *   mvn clean compile exec:java -Dexec.mainClass="assignment4.email_programs.sendEmail" -Dexec.args="thisfile.txt"
 *   Output → In email is sent (i hope lol)
 */
package assignment4.email_programs;

//use of the actual jakarta mail API (FKA javammail)
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class sendEmail {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) { // checking for arguments passed
            System.out.println("Usage: java sendEmail <email file>"); 
            return;
        }

        Map<String, String> emailFields = new HashMap<>(); //holding key-value pairs
        StringBuilder body = new StringBuilder(); // building the actual body of the email
        boolean bodyStart = false;

        for (String line : Files.readAllLines(Paths.get(args[0]))) {
            System.out.println("READ LINE: [" + line + "]"); // Debug line

            if (!bodyStart && line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) { // sepeerating header from body in different parts
                    emailFields.put(parts[0].trim(), parts[1].trim());
                }
            } else {
                bodyStart = true;
                body.append(line).append("\n");
            }
        }

        //Debug: Print parsed fields
        System.out.println("\nEMAIL FIELDS:");
        for (Map.Entry<String, String> entry : emailFields.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        // Check for missing required fields
        String[] requiredKeys = {"Server", "User", "Password", "To", "CC", "BCC", "Subject"}; // getting each individual credential for email
        for (String key : requiredKeys) {
            if (!emailFields.containsKey(key)) {
                System.err.println("Missing required field in input: " + key); // Error trap if input is not given
                return;
            }
        }

    //given each key its appropriate value
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

        Message message = new MimeMessage(session); //creating message and sending it using JakartaMail API
        message.setFrom(new InternetAddress(emailFields.get("User")));
        // giving the information of each recepient specified in text file
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailFields.get("To")));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailFields.get("CC")));
        message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailFields.get("BCC")));
        message.setSubject(emailFields.get("Subject"));
        message.setText(body.toString());

        Transport.send(message);
        System.out.println("Email sent successfully.");
    }
}
