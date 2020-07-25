package ee.fj.utils.mailer.attachment;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PasswordAuthenticator extends Authenticator {
	private final javax.mail.PasswordAuthentication auth;
	public PasswordAuthenticator(javax.mail.PasswordAuthentication auth) {
		this.auth = auth;
	}
	public PasswordAuthenticator(String username, String password) {
		this(new javax.mail.PasswordAuthentication(username, password));
	}

	@Override
    protected PasswordAuthentication getPasswordAuthentication() {
    	return auth;
    }
}
