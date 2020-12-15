package com.foxjunior.io.passwordfile;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
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
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class PasswordProtectedStreamTest {
	@Test
	void defaultAlgorithmExists() {
		Assertions.assertNotNull(SupportedAlgorithm.BEST_ALGORITHM);
	}

	@Test
	void testIO() throws IOException, GeneralSecurityException {
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
			Assertions.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assertions.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assertions.assertNull(in.readLine());
		}
	}


	@Test
	public void testLongSentence() throws IOException, GeneralSecurityException {
		char[] password = "salasona".toCharArray();
		byte[] salt = "12345678".getBytes();
		String algorithm = "PBEWithHmacSHA512AndAES_128";
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PasswordProtectedOutputStream outputStream = new PasswordProtectedOutputStream(byteArrayOutputStream, password, salt, algorithm);

		String[] testStrings = new String[14];
		Arrays.fill(testStrings,  "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer magna purus, vehicula vitae feugiat et, pulvinar quis neque. Quisque porta risus sit amet leo rutrum vulputate. Suspendisse non sollicitudin massa, ac sodales felis. Fusce et laoreet massa. Curabitur blandit turpis justo, nec pretium nisi lobortis sed. Duis consequat nisl in condimentum laoreet. Fusce at tortor purus. Vivamus venenatis eu sapien vitae dapibus. Cras venenatis nulla id urna aliquet, vel rhoncus ligula varius. Nullam euismod urna lacus, vel commodo neque tincidunt in. Sed dictum bibendum sem non rutrum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Suspendisse et sagittis elit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.\n" +
				"\n" +
				"Integer lacinia velit id est congue, iaculis consectetur tortor porta. Donec neque ligula, finibus vitae ornare ut, varius vel odio. In sodales, ex non rutrum imperdiet, libero felis hendrerit ligula, eu rutrum ex ipsum ut erat. Mauris et neque ornare arcu pellentesque ornare. Nullam at dui mauris. Vivamus nec velit felis. Cras id est nibh. Fusce ut velit euismod, tincidunt sem vitae, mollis metus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla egestas at lacus sed pulvinar. Suspendisse id pretium sapien. Mauris malesuada, ex eget placerat consectetur, augue leo semper quam, vel finibus dui dui sed magna. Donec dapibus volutpat mi, eu luctus dolor aliquet porta.\n" +
				"\n" +
				"Maecenas hendrerit enim ut vestibulum volutpat. Nullam molestie a lacus sed ultrices. Etiam dignissim dui ut pulvinar pellentesque. Duis volutpat semper odio eget tempus. Phasellus dignissim et ante non feugiat. In viverra quam ac fermentum finibus. Maecenas lorem nunc, bibendum eu mattis vel, condimentum at tortor. Proin dignissim libero a tempus gravida. Vestibulum quis interdum augue, nec dignissim lectus. Donec non lorem fermentum, luctus ante quis, mattis tellus. Suspendisse lobortis iaculis lacus, ac pellentesque nibh ultricies eget. Suspendisse eros risus, tincidunt id sollicitudin ut, convallis eu elit. In varius turpis arcu, sed luctus tellus tempor eu. Pellentesque a nulla vel enim luctus porttitor.\n" +
				"\n" +
				"Donec eget auctor mi. Phasellus a nulla consequat, suscipit tortor at, dictum lacus. Praesent luctus rutrum neque ac eleifend. Duis nec neque turpis. Vivamus luctus fermentum mauris, vitae iaculis neque vulputate non. Nam interdum mattis lacus, quis efficitur mi ullamcorper eget. Aliquam aliquam nisi in felis ullamcorper, et laoreet augue vulputate. Donec ultricies volutpat lectus. Aenean varius leo enim, quis imperdiet libero feugiat sit amet. Duis suscipit urna felis, vitae viverra arcu pellentesque sed. Praesent semper consectetur ornare. Nullam rutrum nibh id libero hendrerit, eget hendrerit libero cursus. Phasellus facilisis gravida quam, a ultricies libero pellentesque et. Sed eget sapien eleifend massa aliquam dictum nec non mauris.\n" +
				"\n" +
				"Maecenas cursus rutrum ligula vitae tristique. Fusce leo massa, varius vitae pretium quis, tristique fermentum metus. Sed varius purus nulla, sit amet vestibulum ex molestie vel. Maecenas nec dolor sed elit volutpat mattis. Morbi ut ex vitae ligula rhoncus venenatis ut eu leo. Maecenas ligula risus, molestie non nisi a, consectetur tincidunt nisi. Nulla justo libero, ultrices eu enim sed, pellentesque lobortis tellus. Curabitur nunc leo, cursus.");

		for (String s : testStrings) {
			outputStream.write(s.getBytes(StandardCharsets.UTF_8));
		}
		outputStream.close();

		BufferedInputStream inputStream = new BufferedInputStream(new PasswordProtectedInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), password, salt, algorithm));

		for (String s : testStrings) {
			byte[] buffer = new byte[s.getBytes(StandardCharsets.UTF_8).length];
			int len = inputStream.read(buffer);
			Assertions.assertEquals(len, buffer.length);
			String result = new String(buffer, StandardCharsets.UTF_8);
			Assertions.assertEquals(s, result);
		}
		Assertions.assertEquals(-1, inputStream.read());
		inputStream.close();
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
			Assertions.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assertions.assertEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
			Assertions.assertNull(in.readLine());
		}
	}

	@Test
	public void testWrongEncryption() {
		char[] password = "see on salasona".toCharArray();
		byte[] salt = "midaigan".getBytes(StandardCharsets.ISO_8859_1);
		byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		Assertions.assertThrows(NoSuchAlgorithmException.class, () -> {
					try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password, salt, "ahaa")) {
						out.write(dataToWrite);
						out.write('\n');
						out.write(dataToWrite);
					}
				}
		);
	}

	@Test
	public void testWrongSalt() throws IOException, GeneralSecurityException {
		final char[] password = "see on salasona".toCharArray();
		final byte[] salt = PasswordProtectedFactory.getSalt();
		final byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream();
		try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, password, salt)) {
			out.write(dataToWrite);
			out.write('\n');
			out.write(dataToWrite);
		}

		final byte[] wrongSalt = PasswordProtectedFactory.getSalt();
		ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray());
		Exception exception = Assertions.assertThrows(IOException.class, () -> {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, password, wrongSalt),
					StandardCharsets.UTF_8))) {
				Assertions.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
				Assertions.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
				Assertions.assertNull(in.readLine());
			}
		});
		Assertions.assertTrue(GeneralSecurityException.class.isAssignableFrom(exception.getCause().getClass()));
	}

	@Test
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

		final char[] wrongPassword = "".toCharArray();
		ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray());
		Exception exception = Assertions.assertThrows(IOException.class, () -> {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, wrongPassword, salt),
					StandardCharsets.UTF_8))) {
				Assertions.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
				Assertions.assertNotEquals(new String(dataToWrite, StandardCharsets.UTF_8), in.readLine());
				Assertions.assertNull(in.readLine());
			}
		});
		Assertions.assertTrue(GeneralSecurityException.class.isAssignableFrom(exception.getCause().getClass()));
	}

	@Disabled
	@Test
	void testLength() throws IOException, GeneralSecurityException {
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
		for (String alg : new String[]{
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
	void testBytes() throws IOException, GeneralSecurityException {
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
		Assertions.assertEquals(data.length, result.length);

		for (int i = 0; i < data.length; i++) {
			Assertions.assertEquals(data[i], result[i], "At " + i);
		}
	}

}
