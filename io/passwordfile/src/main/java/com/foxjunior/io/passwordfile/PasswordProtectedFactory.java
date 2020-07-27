package com.foxjunior.io.passwordfile;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public final class PasswordProtectedFactory {
	// default salt
	private static final byte[] DEFAULT_SALT = {95, -70, -57, 48, -99 , 38, -126, 117 };

	// default number of iterations
	private static final int DEFAULT_ITERATIONS = 47;


	protected static Cipher getCipher(char[] password, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, DEFAULT_ITERATIONS, SupportedAlgorithm.BEST_ALGORITHM.name(), isEncrypt);
	}

	protected static Cipher getCipher(char[] password, String algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, DEFAULT_ITERATIONS, algorithm, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, SupportedAlgorithm algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		if (!algorithm.supported) {
			throw new NoSuchAlgorithmException(algorithm.name() + " is not supported!");
		}
		return getCipher(password, algorithm.name(), isEncrypt);
	}

	protected static Cipher getCipher(char[] password, byte[] salt, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, salt, DEFAULT_ITERATIONS, SupportedAlgorithm.BEST_ALGORITHM.name(), isEncrypt);
	}

	protected static Cipher getCipher(char[] password, byte[] salt, int iterations, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, salt, iterations, SupportedAlgorithm.BEST_ALGORITHM.name(), isEncrypt);
	}

	protected static Cipher getCipher(char[] password, int iterations, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, iterations, SupportedAlgorithm.BEST_ALGORITHM.name(), isEncrypt);
	}

	protected static Cipher getCipher(char[] password, int iterations, String algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, iterations, algorithm, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, int iterations, SupportedAlgorithm algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		if (!algorithm.supported) {
			throw new NoSuchAlgorithmException(algorithm.name() + " is not supported!");
		}
		return getCipher(password, iterations, algorithm.name(), isEncrypt);
	}

	protected static Cipher getCipher(char[] password, byte[] salt, String algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, salt, DEFAULT_ITERATIONS, algorithm, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, byte[] salt, SupportedAlgorithm algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		if (!algorithm.supported) {
			throw new NoSuchAlgorithmException(algorithm.name() + " is not supported!");
		}
		return getCipher(password, salt, algorithm.name(), isEncrypt);
	}


	protected static Cipher getCipher(char[] password, byte[] salt, int iterations, String algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
	    PBEKeySpec keySpec = new PBEKeySpec(password);
	    SecretKey key = keyFactory.generateSecret(keySpec);

	    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, iterations);

	    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

	    if (isEncrypt) {
	        cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
	    } else {
	        cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
	    }

	    return cipher;
	}

	protected static Cipher getCipher(char[] password, byte[] salt, int iterations, SupportedAlgorithm algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		if (!algorithm.supported) {
			throw new NoSuchAlgorithmException(algorithm.name() + " is not supported!");
		}
		return getCipher(password, salt, iterations, algorithm.name(), isEncrypt);
	}

	public static byte[] getSalt() throws NoSuchAlgorithmException {
		byte[] salt = new byte[8];
		fillWithSalt(salt);
		return salt;
	}


	public static byte[] getSalt(int length) throws NoSuchAlgorithmException {
		byte[] salt = new byte[length];
		fillWithSalt(salt);
		return salt;
	}

	public static void fillWithSalt(byte[] container) throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.nextBytes(container);
	}
}
