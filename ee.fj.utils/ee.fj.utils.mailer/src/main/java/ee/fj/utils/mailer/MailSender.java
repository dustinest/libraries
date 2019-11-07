package ee.fj.utils.mailer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailConnectException;

import ee.fj.utils.mailer.attachment.Attachment;
import ee.fj.utils.mailer.attachment.FileAttachment;
import ee.fj.utils.mailer.attachment.HTMLMailBody;
import ee.fj.utils.mailer.attachment.MailBody;
import ee.fj.utils.mailer.attachment.TextMailBody;

public abstract class MailSender {
	private final MailConfig config;
	private final List<InternetAddress> recipients = new ArrayList<>();
	private final List<InternetAddress> recipientsCC = new ArrayList<>();
	private final List<InternetAddress> recipientsBCC = new ArrayList<>();
	private final List<MailBody> mailBody = new ArrayList<>();
	private final List<Attachment> attachments = new ArrayList<>();
	
	private final String subject;

	protected MailSender(SmtpMailSender config, String subject) {
		if (config.smtpHost == null)
			throw new IllegalArgumentException("Host is not set!");
		if (config.port < 0 )
			throw new IllegalArgumentException("Port is not set!");
		if (config.senderAddress == null) {
			throw new IllegalArgumentException("Sender address is not set!");
		}
		if (subject == null || subject.trim().length() == 0) {
			throw new IllegalArgumentException("Subject is null!");
		}
		this.subject = subject;
		try {
			this.config = (MailConfig)config.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalArgumentException("Cloning is not possible!", e);
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	public MailSender addRecipient(String address) {
		try {
			recipients.add(new InternetAddress(address));
		} catch (AddressException e) {
			throw new IllegalArgumentException("Address " + address + " is invalid!" );
		}
		return this;
	}

	public MailSender addCopyRecipient(String address) {
		try {
			recipientsCC.add(new InternetAddress(address));
		} catch (AddressException e) {
			throw new IllegalArgumentException("Address " + address + " is invalid!" );
		}
		return this;
	}

	public MailSender addBlindCopyRecipient(String address) {
		try {
			recipientsBCC.add(new InternetAddress(address));
		} catch (AddressException e) {
			throw new IllegalArgumentException("Address " + address + " is invalid!" );
		}
		return this;
	}

	public void clearAllRecipients() {
		recipients.clear();
		recipientsBCC.clear();
		recipientsCC.clear();
	}

	@SuppressWarnings("UnusedReturnValue")
	public MailSender addHtmlMailBody(String body) {
		return addMailBody(new HTMLMailBody(body));
	}

	public MailSender addTextMailBody(String body) {
		return addMailBody(new TextMailBody(body));
	}

	private MailSender addMailBody(MailBody body) {
		mailBody.add(body);
		return this;
	}

	public MailSender addAttachement(File file) {
		return addAttachement(new FileAttachment(file));
	}
	
	private MailSender addAttachement(Attachment attachment) {
		attachments.add(attachment);
		return this;
	}

	/**
	 * @throws IllegalStateException if body or recipients are missing
	 * @throws MessagingException if message set fails
	 */
	private Message getMessageContainer() throws IllegalStateException, MessagingException {
		if (mailBody.size() == 0) {
			throw new IllegalStateException("Mail bodies are not set!");
		}
		if (recipients.size() == 0) {
			throw new IllegalStateException("Recipients are not set!");
		}
		Properties props = new Properties();
		Session session;
		if (config.useTls) {
			props.put("mail.smtp.starttls.enable", "true");
		}
		props.put("mail.smtp.host", config.smtpHost);
		props.put("mail.smtp.port", config.port+"");
		if (config.auth != null) {
			props.put("mail.smtp.auth", "true");
			session = Session.getInstance(props, config.auth);
		} else {
			session = Session.getInstance(props);
		}
		Message message = new MimeMessage(session);
		message.setFrom(config.senderAddress);
		if (config.replyAddress != null) {
			message.setReplyTo(new InternetAddress[] {config.replyAddress});
		}
		message.setSubject(subject);
		message.setRecipients(RecipientType.TO, recipients.toArray(new InternetAddress[recipients.size()]));
		if (recipientsCC.size() > 0) {
			message.setRecipients(RecipientType.CC, recipientsCC.toArray(new InternetAddress[recipientsCC.size()]));
		}
		if (recipientsBCC.size() > 0) {
			message.setRecipients(RecipientType.BCC, recipientsBCC.toArray(new InternetAddress[recipientsBCC.size()]));
		}

		MimeMultipart content = new MimeMultipart("mixed");
		for (MailBody body : mailBody) {
			content.addBodyPart(body.getBodyPart());
		}

		for (Attachment a : attachments) {
			content.addBodyPart(a.getBodyPart());
		}

		message.setContent(content);
		return message;
	}

	/**
	 * @throws MessagingException or other failures - this is a fatal error!
	 * @throws SendFailedException if the message could not be sent to some or any of the recipients
	 * @throws IllegalStateException if attributes are wrong or mail settings (java.lang.IllegalArgumentException: port out of range:1232132)
	 * @throws IOException if body parts cannot be set
	 * @throws MailConnectException if connection fails
	 * @throws AuthenticationFailedException This exception is thrown when the connect method on a Store or Transport object fails due to an authentication failure (e.g., bad user name or password).
	 * See more at https://javamail.java.net/nonav/docs/api/index.html?com/sun/mail/util/MailConnectException.html
	 */
	public void send() throws MessagingException, SendFailedException, IllegalStateException, IOException {
		Message message = getMessageContainer();
		Transport.send(message);
	}
}
