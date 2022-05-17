package com.foxjunior.io.passwordfile


import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

import javax.crypto.BadPaddingException
import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.util.stream.IntStream

class PasswordProtectedStreamSpec extends Specification {
	private static final int NEW_LINE = '\n' as char
	private static final PASSWORD = "this is most amazing password".toCharArray()

	def "file is protected by password"() {
		given:
			final byte[] firstLine = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8)
			final byte[] secondLine = "ÜduööKausiäär".getBytes(StandardCharsets.UTF_8)
			ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream()
			try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, PASSWORD)) {
				out.write(firstLine)
				out.write(NEW_LINE)
				out.write(secondLine)
			}
			ByteArrayInputStream outToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray())
		when:
			BufferedReader input = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(outToEncrypt, PASSWORD), StandardCharsets.UTF_8))
		then:
			input.readLine() == new String(firstLine, StandardCharsets.UTF_8)
			input.readLine() == new String(secondLine, StandardCharsets.UTF_8)
			input.readLine() == null
		cleanup:
			input.close()
	}

	def "custom salt works"() {
		given:
			final byte[] salt = "midaigan".getBytes(StandardCharsets.ISO_8859_1)
			final byte[] firstLine = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8)
			final byte[] secondLine = "ÜduööKausiäär".getBytes(StandardCharsets.UTF_8)
			final ByteArrayOutputStream outToEncrpt = new ByteArrayOutputStream()
			try (OutputStream out = new PasswordProtectedOutputStream(outToEncrpt, PASSWORD, salt)) {
				out.write(firstLine)
				out.write(NEW_LINE)
				out.write(secondLine)
			}
			final ByteArrayInputStream outToEncrypt = new ByteArrayInputStream(outToEncrpt.toByteArray())
		when:
			final BufferedReader input = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(outToEncrypt, PASSWORD, salt), StandardCharsets.UTF_8))
		then:
			input.readLine() == new String(firstLine, StandardCharsets.UTF_8)
			input.readLine() == new String(secondLine, StandardCharsets.UTF_8)
			input.readLine() == null
		cleanup:
			input.close()
	}

	def "Wrong encryption with salt"() {
		given:
			final byte[] salt = "midaigan".getBytes(StandardCharsets.ISO_8859_1)
			final ByteArrayOutputStream outToEncrypt = new ByteArrayOutputStream()
		when:
			new PasswordProtectedOutputStream(outToEncrypt, PASSWORD, salt, "ahaa")
		then:
			Exception error = thrown(NoSuchAlgorithmException.class)
			error.message == "ahaa SecretKeyFactory not available"
	}

	def "Wrong encryption without salt"() {
		given:
			final ByteArrayOutputStream outToEncrypt = new ByteArrayOutputStream()
		when:
			new PasswordProtectedOutputStream(outToEncrypt, PASSWORD, "ahaa")
		then:
			Exception error = thrown(NoSuchAlgorithmException.class)
			error.message == "ahaa SecretKeyFactory not available"
	}

	def "When salt is wrong then data is scrambled and error is thrown"() {
		given:
			final byte[] salt = PasswordProtectedFactory.getSalt()
			final byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8)
			final ByteArrayOutputStream outToEncrypt = new ByteArrayOutputStream()
			try (OutputStream out = new PasswordProtectedOutputStream(outToEncrypt, PASSWORD, salt)) {
				out.write(dataToWrite)
				out.write(NEW_LINE)
				out.write(dataToWrite)
			}
			final byte[] wrongSalt = PasswordProtectedFactory.getSalt()
			final ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrypt.toByteArray())
		when:
			def reader = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, PASSWORD, wrongSalt), StandardCharsets.UTF_8))
			reader.readLine()
		then:
			Exception error = thrown(IOException)
			error.cause instanceof BadPaddingException
			error.message == "javax.crypto.BadPaddingException: Given final block not properly padded. Such issues can arise if a bad key is used during decryption."
		cleanup:
			reader.close()
	}

	def "When password is wrong"() {
		given:
			final byte[] salt = PasswordProtectedFactory.getSalt()
			final byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8)
			final ByteArrayOutputStream outToEncrypt = new ByteArrayOutputStream()
			try (OutputStream out = new PasswordProtectedOutputStream(outToEncrypt, PASSWORD, salt)) {
				out.write(dataToWrite)
				out.write(NEW_LINE)
				out.write(dataToWrite)
			}
			final char[] wrongPassword = "".toCharArray()
			ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrypt.toByteArray())
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new PasswordProtectedInputStream(inToEncrypt, wrongPassword, salt), StandardCharsets.UTF_8))
		when:
			reader.readLine() // first line might be a bit flaky but still returns scrambled result
			reader.readLine()
		then:
			Exception error = thrown(IOException)
			error.cause instanceof BadPaddingException
			error.message == "javax.crypto.BadPaddingException: Given final block not properly padded. Such issues can arise if a bad key is used during decryption."
	}

	@Unroll
	@Ignore // this test takes too long and can fail in various platforms
	def "can use encoding #encoding"() {
		given:
			final byte[] salt = PasswordProtectedFactory.getSalt()
			for (byte b : salt) {
				System.out.print(b)
				System.out.print(' ' as char)
				System.out.print(',' as char)
			}
			byte[] dataToWrite = "Lorem Ipsum Est ÕÜÖÜ".getBytes(StandardCharsets.UTF_8)
			final ByteArrayOutputStream outToEncrypt = new ByteArrayOutputStream()
			OutputStream outputStream = new PasswordProtectedOutputStream(outToEncrypt, PASSWORD, salt, encoding)
		when:
			IntStream.range(1, 100000).forEach{e ->
				outputStream.write(dataToWrite)
				outputStream.write(NEW_LINE)
			}
		then:
			true
		cleanup:
			outputStream.close()
		where:
			encoding << ["PBEWithHmacSHA512AndAES_256", // SHA512
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
			             "PBEWithSHA1AndPC4_128"]
	}

	def "bytes can be written"() {
		given:
			final byte[] data = new byte[100000]
			new Random().nextBytes(data)
			final ByteArrayOutputStream outToEncrypt = new ByteArrayOutputStream()
			try (OutputStream out = new PasswordProtectedOutputStream(outToEncrypt, PASSWORD)) {
				out.write(data)
				out.flush()
			}
			final ByteArrayInputStream inToEncrypt = new ByteArrayInputStream(outToEncrypt.toByteArray())
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
			final InputStream inputStream = new PasswordProtectedInputStream(inToEncrypt, PASSWORD)
			final byte[] buffer = new byte[256]
		when:
			for (int l = inputStream.read(buffer); l >= 0; l = inputStream.read(buffer)) {
				outputStream.write(buffer, 0, l)
			}
			byte[] result = outputStream.toByteArray()
		then:
			data.length == result.length
		expect:
			for (int i = 0; i < data.length; i++) {
				assert data[i] == result[i], [data[i], result[i], "At index" + i]
			}
		cleanup:
			inputStream.close()
	}
}
