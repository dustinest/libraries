# Javamail wrapper to send mails

## Simple usage:

First create a smtp configuration

		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setSender("my.sender@mytest.com");
		smtpSender.setUseTls(false);

You might want to authorise

		smtpSender.authorize(username, String);

Or use TLS
	
		smtpSender.setUseTls(true);


Now get the instance of the mail sender using the subject

		MailSender sender = smtpSender.getMailSender("My Test subject");

Set body by type

		sender.addHtmlMailBody("<p>Lorem ipsum Est</p>");
		sender.addTextMailBody("Lorem ipsum Est");

Add whom do you send it

		sender.addRecipient("name@domain.com");
		sender.addCopyRecipient("CCname@domain.com");
		sender.addBlindCopyRecipient("BCCname@domain.com");

Add attachments

		sender.addAttachement(File file);

Add Custom attachment

		sender.addAttachement(Attachment attachment);

And simply just send

		sender.send();

