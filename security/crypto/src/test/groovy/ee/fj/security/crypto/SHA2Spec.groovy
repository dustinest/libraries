package ee.fj.security.crypto

import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class SHA2Spec extends Specification {
	private static final String TEST_STRING = "Lorem Ipsum Est"

	@Unroll
	def "Algorithm #algorithm works"() {
		when:
			def resultBytes = algorithm.bytesResult().getHashFromString(TEST_STRING)
		then:
			resultBytes.length == length
			resultBytes == expectedBytes
		where:
			algorithm     | length | expectedBytes
			SHA2.HASH_224 | 28     | [31, 117, 108, 46, -13, 41, -14, 54, -78, 120, 98, -71, -19, -53, -125, 61, -46, 98, -108, -6, -115, -27, 38, 48, -17, 59, 21, -9]  as byte[]
			SHA2.HASH_256 | 32     | [-59, -88, 98, 108, 46, -58, 8, 120, 94, 39, 108, -48, -20, 109, -68, -28, 93, 95, 16, 41, -10, -5, 30, -110, -80, -58, -112, 11, 79, -83, -110, -113]  as byte[]
			SHA2.HASH_384 | 48     | [-42, 14, -106, 24, -121, 21, 111, -62, 101, 53, -75, 44, 38, 50, 122, 121, 70, -21, 39, 48, 123, -35, -41, 32, 94, 96, 119, -36, -41, 116, 56, 44, 46, 105, 10, -56, -122, -83, 52, 117, 45, 81, 85, 102, 39, 86, -15, 118]  as byte[]
			SHA2.HASH_512 | 64     | [59, 49, -63, -113, 74, -59, 24, 15, 57, -8, 13, 111, 99, -64, 112, 30, -60, 58, 117, -118, -58, -29, -22, 19, 29, -4, -58, 42, -26, 89, -21, -90, 14, 23, -24, -14, -28, -35, 24, -111, -120, -21, 64, -120, -48, -98, 101, 2, -29, -30, 2, -58, -105, -79, 24, -71, 8, 72, -19, 47, 116, -101, 65, -120]  as byte[]
	}

	@Unroll
	def "Algorithm #algorithm works with String"() {
		when:
			def resultBytes = algorithm.bytesResult().getHashFromString(TEST_STRING)
			def bytesToHex = BytesToHex.bytesToHex(resultBytes)
			def resultHex = algorithm.stringResult().getHashFromString(TEST_STRING)
		then:
			bytesToHex == resultHex
			resultHex.size() == size
			resultHex == expected
		where:
			algorithm     | size | expected
			SHA2.HASH_224 | 56   | "1f756c2ef329f236b27862b9edcb833dd26294fa8de52630ef3b15f7"
			SHA2.HASH_256 | 64   | "c5a8626c2ec608785e276cd0ec6dbce45d5f1029f6fb1e92b0c6900b4fad928f"
			SHA2.HASH_384 | 96   | "d60e961887156fc26535b52c26327a7946eb27307bddd7205e6077dcd774382c2e690ac886ad34752d5155662756f176"
			SHA2.HASH_512 | 128  | "3b31c18f4ac5180f39f80d6f63c0701ec43a758ac6e3ea131dfcc62ae659eba60e17e8f2e4dd189188eb4088d09e6502e3e202c697b118b90848ed2f749b4188"
	}

	def "SHA-256 is not the same as SHA3"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA3-256")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA2.HASH_256.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes.length == 32
			javaBytes != resultBytes
			resultBytes == [-59, -88, 98, 108, 46, -58, 8, 120, 94, 39, 108, -48, -20, 109, -68, -28, 93, 95, 16, 41, -10, -5, 30, -110, -80, -58, -112, 11, 79, -83, -110, -113] as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = SHA2.HASH_256.stringResult().getHashFromString(TEST_STRING)
		then:
			javaHex != resultHex
			javaHex.length() == resultHex.length()
			javaHex == "2527da118865db5778a35469bcd6fed31278d34db69895934fce6686f0c76a6c"
			resultHex == "c5a8626c2ec608785e276cd0ec6dbce45d5f1029f6fb1e92b0c6900b4fad928f"
	}

	def "JDK SHA3-256 is not the same as SHA2"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA3-256")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA2.HASH_256.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes.length == 32
			javaBytes != resultBytes
			resultBytes == [-59, -88, 98, 108, 46, -58, 8, 120, 94, 39, 108, -48, -20, 109, -68, -28, 93, 95, 16, 41, -10, -5, 30, -110, -80, -58, -112, 11, 79, -83, -110, -113] as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = SHA2.HASH_256.stringResult().getHashFromString(TEST_STRING)
		then:
			javaHex != resultHex
			javaHex.length() == resultHex.length()
			javaHex == "2527da118865db5778a35469bcd6fed31278d34db69895934fce6686f0c76a6c"
			resultHex == "c5a8626c2ec608785e276cd0ec6dbce45d5f1029f6fb1e92b0c6900b4fad928f"
	}

	def "JDK SHA2-256 is SHA2"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA-256")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA2.HASH_256.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes.length == 32
			javaBytes == resultBytes
			resultBytes == [-59, -88, 98, 108, 46, -58, 8, 120, 94, 39, 108, -48, -20, 109, -68, -28, 93, 95, 16, 41, -10, -5, 30, -110, -80, -58, -112, 11, 79, -83, -110, -113]  as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = SHA2.HASH_256.stringResult().getHashFromString(TEST_STRING)
		then:
			javaHex == resultHex
			javaHex.length() == resultHex.length()
			resultHex == "c5a8626c2ec608785e276cd0ec6dbce45d5f1029f6fb1e92b0c6900b4fad928f"
	}


	def "JDK SHA3-512 is SHA3"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA-512")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA2.HASH_512.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes == resultBytes
			resultBytes == [59, 49, -63, -113, 74, -59, 24, 15, 57, -8, 13, 111, 99, -64, 112, 30, -60, 58, 117, -118, -58, -29, -22, 19, 29, -4, -58, 42, -26, 89, -21, -90, 14, 23, -24, -14, -28, -35, 24, -111, -120, -21, 64, -120, -48, -98, 101, 2, -29, -30, 2, -58, -105, -79, 24, -71, 8, 72, -19, 47, 116, -101, 65, -120] as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = SHA2.HASH_512.stringResult().getHashFromString(TEST_STRING)
		then:
			javaHex == resultHex
			javaHex.length() == resultHex.length()
			resultHex == "3b31c18f4ac5180f39f80d6f63c0701ec43a758ac6e3ea131dfcc62ae659eba60e17e8f2e4dd189188eb4088d09e6502e3e202c697b118b90848ed2f749b4188"
	}

}
