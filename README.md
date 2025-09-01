Java Email Application

This project is a Java-based email application designed to demonstrate how to send, receive, and manage emails over the Internet using standard email servers. The system is divided into three programs, each building on the previous one to cover key aspects of email functionality.

The first program, SendEmail, connects to a specified email server and sends a message to a primary recipient, multiple secondary (CC) recipients, and multiple tertiary (BCC) recipients. All email details, including server settings, user credentials, recipients, subject, and body, are provided in a text file that is read at runtime. This approach makes the program flexible and easy to test with different email accounts and configurations.

The second program extends the first by adding support for file attachments. At a minimum, the program can handle one attachment, such as an image file (.jpg, .png) or a compressed file (.zip). This addition demonstrates how Java can construct and send multipart emails that include both text and binary content.

The third program, GetMail, focuses on receiving messages from an email server. When run with user credentials, it retrieves a list of unread emails and displays them in a numbered format, including the subject line and sender. Users can then rerun the program with the corresponding number to download and view the full contents of a specific email. This simulates a simple email client that provides basic inbox functionality.

Together, these three programs illustrate the essentials of email communication in Java: sending structured messages, handling attachments, and retrieving incoming mail. By working through the project, you gain practical experience with Java’s networking and email APIs, while also learning how real-world email systems operate under the hood.

=============================================================================================================================================================================================

Here are the key features of your Java Email App that you can list on GitHub:

Send Email from File Input – Reads server, user credentials, recipients, subject, and body from a text file and sends the email automatically.

Multiple Recipient Support – Allows one primary recipient, multiple CC (secondary) recipients, and multiple BCC (tertiary) recipients.

Attachment Handling – Supports sending a file attachment (image or .zip) along with the email.

Inbox Retrieval – Connects to an email server to fetch a list of unread emails, showing subject lines and senders.

Email Selection – Users can download and view the full content of a chosen email by specifying its reference number.

Command-Line Execution – All programs are run via simple command-line commands for flexibility and testing.

Practical Email Protocol Use – Demonstrates how to interact with email servers for both sending (SMTP) and receiving (POP/IMAP).
