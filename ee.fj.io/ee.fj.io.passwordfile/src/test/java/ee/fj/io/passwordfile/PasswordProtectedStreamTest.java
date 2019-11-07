package ee.fj.io.passwordfile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class PasswordProtectedStreamTest {
	@Test
	public void defaultAlgorithmExists() {
		Assert.assertNotNull(SupportedAlgorithm.BEST_ALGORITHM);
	}
	
	@Test
	public void testIO() throws IOException, GeneralSecurityException {
		char[] password = "see on salasona".toCharArray();
		byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password)) {
			out.write(dataToWrite);
			out.write('\n');
			out.write(dataToWrite);
		}

		ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray());
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, password),
				StandardCharsets.UTF_8))) {
			Assert.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertNull(in.readLine());
		}
	}

	@Test
	public void testCustomSalt() throws IOException, GeneralSecurityException {
		char[] password = "see on salasona".toCharArray();
		byte[] salt = "midaigan".getBytes(StandardCharsets.ISO_8859_1);
		byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password, salt)) {
			out.write(dataToWrite);
			out.write('\n');
			out.write(dataToWrite);
		}

		ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray());
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, password, salt),
				StandardCharsets.UTF_8))) {
			Assert.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertNull(in.readLine());
		}
	}

	@Test(expected=NoSuchAlgorithmException.class)
	public void testWrongEncryption() throws IOException, GeneralSecurityException {
		char[] password = "see on salasona".toCharArray();
		byte[] salt = "midaigan".getBytes(StandardCharsets.ISO_8859_1);
		byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password, salt, "ahaa")) {
			out.write(dataToWrite);
			out.write('\n');
			out.write(dataToWrite);
		}
	}

	@Test(expected=IOException.class) // caused by GeneralSecurityException
	public void testWrongSalt() throws IOException, GeneralSecurityException {
		char[] password = "see on salasona".toCharArray();
		byte[] salt = PasswordProtectedFactory.getSalt();
		byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password, salt)) {
			out.write(dataToWrite);
			out.write('\n');
			out.write(dataToWrite);
		}
		salt = PasswordProtectedFactory.getSalt();
		ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray());
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, password, salt),
				StandardCharsets.UTF_8))) {
			Assert.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertNull(in.readLine());
		}
	}

	@Test(expected=IOException.class) // caused by GeneralSecurityException
	public void wrongPassword() throws IOException, GeneralSecurityException {
		char[] password = "see on salasona".toCharArray();
		byte[] salt = PasswordProtectedFactory.getSalt();
		byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password, salt)) {
			out.write(dataToWrite);
			out.write('\n');
			out.write(dataToWrite);
		}
		password = "".toCharArray();
		ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray());
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, password, salt),
				StandardCharsets.UTF_8))) {
			Assert.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assert.assertNull(in.readLine());
		}
	}

	@Ignore
	@Test
	public void testLength() throws IOException, GeneralSecurityException {
		char[] password = "see on salasona".toCharArray();
		byte[] salt = PasswordProtectedFactory.getSalt();
		for (byte b : salt) {
			System.out.print(b);
			System.out.print(' ');
			System.out.print(',');
		}
		System.out.println();
		byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		for (String alg : new String[] {
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
				"PBEWithMD5AndTripleDES", // insecure
				"PBEWithSHA1AndDESede", // SHA1 weak
				"PBEWithMD5AndDES", // insecure
				"PBEWithSHA1AndRC2_40",
				"PBEWithSHA1AndRC2_128",
				"PBEWithSHA1AndRC4_40",
				"PBEWithSHA1AndPC4_128"				
		}) {
			outToEncrpt.reset();
			try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password, salt, alg)) {
				long start = System.currentTimeMillis();
				IntStream.range(1, 100000).forEach(e -> {
					try {
						out.write(dataToWrite);
						out.write('\n');
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
				System.out.println(outToEncrpt.size() + " - " + (System.currentTimeMillis() - start + ": " + alg));
			} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
				System.out.println(alg + ": " + e.getMessage());
			}
		}

	}

	@Test
	public void testBytes() throws IOException, GeneralSecurityException {
		Random rnd = new Random();
		byte[] data = new byte[100000];
		rnd.nextBytes(data);

		char[] password = "see on salasona".toCharArray();
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password)) {
			out.write(data);
			out.flush();
		}

		ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray());
		ByteArrayOutputStream _data = new ByteArrayOutputStream();
		try (InputStream in = new PasswordProtectedInputStream(inToEncrypt, password)) {
			byte[] buffer = new byte[256];
			for (int l = in.read(buffer); l >= 0; l = in.read(buffer)) {
				_data.write(buffer, 0, l);
			}
		}
		byte[] result = _data.toByteArray();
		Assert.assertEquals(data.length, result.length);

		for (int i = 0; i < data.length; i++) {
			Assert.assertEquals("At " + i, data[i], result[i]);
		}
	}

}
