package ee.fj.utils.mailer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import ee.fj.utils.mailer.attachment.PasswordAuthenticator;

public class SmtpMailSender extends MailConfig {

	@SuppressWarnings("EmptyMethod")
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setHost(String host) {
		this.smtpHost = host;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public void setSender(String senderAdderss) throws IllegalArgumentException {
		try {
			this.senderAddress = new InternetAddress(senderAdderss);
		} catch (AddressException e) {
			throw new IllegalArgumentException("Address " + senderAdderss + " is invalid!" );
		}
	}

	public void setSender(String senderAdderss, String senderName) throws IllegalArgumentException {
		try {
			this.senderAddress = new InternetAddress(senderAdderss, senderName, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Name " + senderAdderss + " is invalid!" );
		}
	}

	public void setReplyAddress(String replyAddress) throws IllegalArgumentException {
		try {
			this.replyAddress = new InternetAddress(replyAddress);
		} catch (AddressException e) {
			throw new IllegalArgumentException("Address " + replyAddress + " is invalid!" );
		}
	}

	public void setReplyAddress(String replyAddress, String replyToName) throws IllegalArgumentException {
		try {
			this.replyAddress = new InternetAddress(replyAddress, replyToName, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Name " + replyToName + " is invalid!" );
		}
	}

	public void removeAuthorization() {
		this.auth = null;
	}
	
	public void authorize(String username, String password) {
		this.auth = new PasswordAuthenticator(username, password);
	}

	public void setUseTls(boolean value) {
		useTls = value;
	}

	public MailSender getMailSender(String subject) {
		return new MailSender(this, subject) {};
	}

	
}
