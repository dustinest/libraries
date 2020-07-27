package com.foxjunior.io.passwordfile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import javax.crypto.CipherOutputStream;

public class PasswordProtectedOutputStream extends CipherOutputStream {
	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password) throws GeneralSecurityException {
		super(outputStream, PasswordProtectedFactory.getCipher(password, true));
	}

	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password, String algorithm) throws GeneralSecurityException {
		super(outputStream, PasswordProtectedFactory.getCipher(password, algorithm, true));
	}

	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password, byte[] salt, int iterations, boolean isEncrypt) throws GeneralSecurityException {
		super(outputStream, PasswordProtectedFactory.getCipher(password, salt, iterations, true));
	}

	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password, int iterations) throws GeneralSecurityException {
		super(outputStream, PasswordProtectedFactory.getCipher(password, iterations, true));
	}

	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password, int iterations, String algorithm) throws GeneralSecurityException {
		super(outputStream, PasswordProtectedFactory.getCipher(password, iterations, algorithm, true));
	}


	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password, byte[] salt) throws GeneralSecurityException {
		super(outputStream, PasswordProtectedFactory.getCipher(password, salt, true));
	}

	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password, byte[] salt, String algorithm) throws GeneralSecurityException {
		super(outputStream, PasswordProtectedFactory.getCipher(password, salt, algorithm, true));
	}

	public PasswordProtectedOutputStream(OutputStream outputStream, char[] password, byte[] salt, int iterations, String algorithm) throws GeneralSecurityException {
		 super(outputStream, PasswordProtectedFactory.getCipher(password, salt, iterations, algorithm, true));
	}

	@Override
	public void close() throws IOException {
		super.flush();
		super.close();
	}

}
