package ee.fj.io.passwordfile;

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
	private static final String DEFAULT_ALGORITHM;
	static {
		String alg = null;
		for (String a : new String[] {
				"PBEWithHmacSHA512AndAES_256", // SHA512
				"PBEWithHmacSHA512AndAES_128", // SHA512
				"PBEWithHmacSHA384AndAES_256", // SHA384
				"PBEWithHmacSHA384AndAES_128", // SHA384
				"PBEWithHmacSHA256AndAES_256", // SHA256
				"PBEWithHmacSHA256AndAES_128", // SHA256
				"PBEWithHmacSHA224AndAES_256", // SHA224
				"PBEWithHmacSHA224AndAES_128", // SHA224
				// weaks:
				"PBEWithHmacSHA1AndAES_128",
				"PBEWithHmacSHA1AndAES_256", // SHA1 weak
				"PBEWithSHA1AndRC2_40",
				"PBEWithSHA1AndRC4_40",
				"PBEWithSHA1AndRC2_128",
				"PBEWithSHA1AndPC4_128"	,			
				"PBEWithSHA1AndDESede", // SHA1 weak
				"PBEWithMD5AndTripleDES", // insecure
				"PBEWithMD5AndDES" // insecure
		}) {
			try {
				SecretKeyFactory.getInstance(a);
				alg = a;
				break;
			} catch (NoSuchAlgorithmException e) {
				
			}
		}
		DEFAULT_ALGORITHM = alg;
	}

	// default salt
	private static final byte[] DEFAULT_SALT = {95, -70, -57, 48, -99 , 38, -126, 117 };

	// default number of iterations
	private static final int DEFAULT_ITERATIONS = 47;


	protected static Cipher getCipher(char[] password, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, DEFAULT_ITERATIONS, DEFAULT_ALGORITHM, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, String algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, DEFAULT_ITERATIONS, algorithm, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, byte[] salt, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, salt, DEFAULT_ITERATIONS, DEFAULT_ALGORITHM, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, byte[] salt, int iterations, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, salt, iterations, DEFAULT_ALGORITHM, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, int iterations, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, iterations, DEFAULT_ALGORITHM, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, int iterations, String algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, DEFAULT_SALT, iterations, algorithm, isEncrypt);
	}

	protected static Cipher getCipher(char[] password, byte[] salt, String algorithm, boolean isEncrypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		return getCipher(password, salt, DEFAULT_ITERATIONS, algorithm, isEncrypt);
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
