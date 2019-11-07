package ee.fj.utils.mailer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import ee.fj.utils.mailer.attachment.PasswordAuthenticator;

public class MailConfig implements Cloneable {
	private static final Pattern MAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	private static final Pattern SERVER_PATTERN = Pattern.compile("^[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	String smtpHost;
	int port = -1;
	PasswordAuthenticator auth;
	InternetAddress senderAddress;
	InternetAddress replyAddress;
	boolean useTls = Boolean.FALSE;

	public static boolean isValidMail(String mail) {
		if (mail == null || mail.trim().length() == 0)
			return false;
		Matcher matcher = MAIL_PATTERN.matcher(mail);
		if (!matcher.matches()) {
			return false;
		}
		try {
			new InternetAddress(mail);
		} catch (AddressException e) {
			return false;
		}
		return true;
	}

	public static boolean isServerValid(String server) {
		if (server == null || server.trim().length() == 0)
			return false;
		return SERVER_PATTERN.matcher(server).matches();
	}

}
