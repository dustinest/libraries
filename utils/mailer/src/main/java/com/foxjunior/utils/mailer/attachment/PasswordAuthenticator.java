package com.foxjunior.utils.mailer.attachment;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

public class PasswordAuthenticator extends Authenticator {
	private final PasswordAuthentication auth;
	public PasswordAuthenticator(PasswordAuthentication auth) {
		this.auth = auth;
	}
	public PasswordAuthenticator(String username, String password) {
		this(new PasswordAuthentication(username, password));
	}

	@Override
    protected PasswordAuthentication getPasswordAuthentication() {
    	return auth;
    }
}
