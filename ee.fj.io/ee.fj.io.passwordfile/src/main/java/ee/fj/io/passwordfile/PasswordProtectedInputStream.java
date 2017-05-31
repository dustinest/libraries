package ee.fj.io.passwordfile;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import javax.crypto.CipherInputStream;

public class PasswordProtectedInputStream extends CipherInputStream {
	public PasswordProtectedInputStream(InputStream inputStream, char[] password) throws GeneralSecurityException, IOException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, String algorithm) throws GeneralSecurityException, IOException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, algorithm, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, SupportedAlgorithm algorithm) throws GeneralSecurityException, IOException {
		this(inputStream, password, algorithm.name());
	}
	

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt, int iterations, boolean isEncrypt) throws GeneralSecurityException, IOException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, salt, iterations, false));
	}

	
	public PasswordProtectedInputStream(InputStream inputStream, char[] password, int iterations) throws GeneralSecurityException, IOException {
		super(inputStream,PasswordProtectedFactory.getCipher(password, iterations, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, int iterations, String algorithm) throws GeneralSecurityException, IOException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, iterations, algorithm, false));
	}


	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt) throws GeneralSecurityException, IOException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, salt, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt, String algorithm) throws GeneralSecurityException, IOException {
		super(inputStream, PasswordProtectedFactory.getCipher(password, salt, algorithm, false));
	}

	public PasswordProtectedInputStream(InputStream inputStream, char[] password, byte[] salt, int iterations, String algorithm) throws GeneralSecurityException, IOException {
	    super(inputStream, PasswordProtectedFactory.getCipher(password, salt, iterations, algorithm, false));
	}
}
