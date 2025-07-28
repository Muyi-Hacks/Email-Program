sendEmail.java
This program reads an email template from a file and sends a plain-text email using the Jakarta Mail (formerly JavaMail) API.

Features:
Parses required fields like Server, User, Password, To, CC, BCC, and Subject

Extracts the rest of the file as the email body

Sends the email using SMTP with TLS (port 587)

Debug output to verify parsed data

Validates presence of all required fields

sendEmailAtt.java
An extension of sendEmail.java that supports email attachments.

Additional Features:
Parses an optional Attachment field from the input file

Validates the file path before sending

Uses a MimeMultipart to attach the file to the email

Still supports all base features: To, CC, BCC, Subject, Body

getMail.java
This program connects to an IMAP server and retrieves unread emails.

Features:
Takes server, username, and password as input

Connects to INBOX using imaps (SSL)

Lists unread emails with sender and subject

Can also display the full content of a selected unread email

Handles both plain text and multipart emails

Supports attachments (prints notice if attachments are found)

⚠This program is only briefly referenced here. Please see inline comments within getMail.java for more details.
