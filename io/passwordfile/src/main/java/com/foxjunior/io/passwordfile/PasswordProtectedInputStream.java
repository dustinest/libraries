package com.foxjunior.io.passwordfile;

import java.io.InputStream;
import java.security.GeneralSecurityException;

import javax.crypto.CipherInputStream;

public class PasswordProtectedInputStream extends CipherInputStream {
	public PasswordProtectedInputStream(InputStream inputStream, char[] password) throws GeneralSecurityException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, String algorithm) throws GeneralSecurityException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, algorithm, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, SupportedAlgorithm algorithm) throws GeneralSecurityException {
		this(inputStream, password, algorithm.name());
	}


	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt, int iterations, boolean isEncrypt) throws GeneralSecurityException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, salt, iterations, false));
	}


	public PasswordProtectedInputStream(InputStream inputStream, char[] password, int iterations) throws GeneralSecurityException {
		super(inputStream,PasswordProtectedFactory.getCipher(password, iterations, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, int iterations, String algorithm) throws GeneralSecurityException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, iterations, algorithm, false));
	}


	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt) throws GeneralSecurityException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, salt, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt, String algorithm) throws GeneralSecurityException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, salt, algorithm, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt, int iterations, String algorithm) throws GeneralSecurityException {
	    super(inputStream, PasswordProtectedFactory.getCipher(password, salt, iterations, algorithm, false));
	}
}
