/*
 * Name: Muyi Omorogbe
 * Date: 06/20/25
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
 *
 * Example Usage:
 *   mvn exec:java -Dexec.mainClass="assignment4.email_programs.getMail" \-Dexec.args="imap.gmail.com xxxxxxxx xxxxxxx 3"
 *   Output → In email is sent (i hope lol)
 */
package assignment4.email_programs;

//jakarta mail api being used

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;

import java.util.Properties;

public class getMail {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) { 
            System.out.println("Usage: java getMail <server> <user> <password> [messageNumber]");
            return;
        }

        String host = args[0];
        String user = args[1];
        String password = args[2];

        // IMAPS setup (SSL for Gmail)
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect(host, user, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        // Search for unseen (unread) emails
        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        if (args.length == 3) {
            // Printing a  list of unread messages
            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                Address[] from = msg.getFrom();
                String sender = (from != null && from.length > 0) ? from[0].toString() : "Unknown";
                System.out.printf("%d. %s (%s)%n", i + 1, msg.getSubject(), sender);
            }
        } else {
            // This is for getting the specific messafe like asked in the question
            int index = Integer.parseInt(args[3]) - 1;
            if (index < 0 || index >= messages.length) {
                System.out.println("Invalid message number.");
                return;
            }

            Message msg = messages[index]; // printing content we are getting from the unread emails
            System.out.println("Subject: " + msg.getSubject());
            System.out.println("From: " + msg.getFrom()[0]);
            System.out.println("Content:\n");

            Object content = msg.getContent();
            if (content instanceof String) {
                System.out.println(content);
            } else if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart part = multipart.getBodyPart(i);
                    if (part.getContentType().contains("text/plain")) {
                        System.out.println(part.getContent());
                    }
                }
            }
        }

        inbox.close(false);
        store.close();
    }
}
