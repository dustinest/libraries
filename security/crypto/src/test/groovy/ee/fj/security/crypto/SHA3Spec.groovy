package ee.fj.security.crypto

import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class SHA3Spec extends Specification {
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
			SHA3.HASH_224 | 28     | [-66, -58, -76, -22, -43, -77, -16, -115, -85, -5, 119, -82, -121, 58, -2, 88, 79, 100, -41, 15, 30, 40, -20, 82, -30, 45, -120, -80] as byte[]
			SHA3.HASH_256 | 32     | [37, 39, -38, 17, -120, 101, -37, 87, 120, -93, 84, 105, -68, -42, -2, -45, 18, 120, -45, 77, -74, -104, -107, -109, 79, -50, 102, -122, -16, -57, 106, 108] as byte[]
			SHA3.HASH_384 | 48     | [-16, -112, 2, -14, -31, -121, 18, -57, 80, 37, 18, -24, -119, 54, -31, 56, -76, 46, 40, 37, 23, 71, 109, 50, -125, -40, -65, 126, -100, -35, 31, -125, -86, -31, 83, -120, 30, 54, -32, -27, 15, 54, -75, -71, -27, 41, 97, -93] as byte[]
			SHA3.HASH_512 | 64     | [126, -127, -126, 120, 59, 1, 65, 59, -121, -27, 127, -40, -33, -81, 98, -27, 25, -3, -27, 62, 62, -125, 7, -58, 19, -59, 117, 120, -104, 89, -41, 63, 126, 13, -104, -20, 41, -50, -93, 12, -45, 63, 60, -95, 79, -26, 122, -17, -71, -23, 46, -13, -100, -124, 13, 42, -4, -72, 113, 118, 3, 63, 115, -69] as byte[]
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
			SHA3.HASH_224 | 56   | "bec6b4ead5b3f08dabfb77ae873afe584f64d70f1e28ec52e22d88b0"
			SHA3.HASH_256 | 64   | "2527da118865db5778a35469bcd6fed31278d34db69895934fce6686f0c76a6c"
			SHA3.HASH_384 | 96   | "f09002f2e18712c7502512e88936e138b42e282517476d3283d8bf7e9cdd1f83aae153881e36e0e50f36b5b9e52961a3"
			SHA3.HASH_512 | 128  | "7e8182783b01413b87e57fd8dfaf62e519fde53e3e8307c613c575789859d73f7e0d98ec29cea30cd33f3ca14fe67aefb9e92ef39c840d2afcb87176033f73bb"
	}

	def "SHA-256 is not the same as SHA3"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA-256")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA3.HASH_256.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes.length == 32
			javaBytes != resultBytes
			resultBytes == [37, 39, -38, 17, -120, 101, -37, 87, 120, -93, 84, 105, -68, -42, -2, -45, 18, 120, -45, 77, -74, -104, -107, -109, 79, -50, 102, -122, -16, -57, 106, 108] as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = BytesToHex.bytesToHex(resultBytes)
		then:
			javaHex != resultHex
			javaHex.length() == resultHex.length()
			javaHex == "c5a8626c2ec608785e276cd0ec6dbce45d5f1029f6fb1e92b0c6900b4fad928f"
			resultHex == "2527da118865db5778a35469bcd6fed31278d34db69895934fce6686f0c76a6c"
	}

	def "JDK SHA2-256 is not the same as SHA3"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA-256")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA3.HASH_256.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes.length == 32
			javaBytes != resultBytes
			resultBytes == [37, 39, -38, 17, -120, 101, -37, 87, 120, -93, 84, 105, -68, -42, -2, -45, 18, 120, -45, 77, -74, -104, -107, -109, 79, -50, 102, -122, -16, -57, 106, 108] as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = BytesToHex.bytesToHex(resultBytes)
		then:
			javaHex != resultHex
			javaHex.length() == resultHex.length()
			javaHex == "c5a8626c2ec608785e276cd0ec6dbce45d5f1029f6fb1e92b0c6900b4fad928f"
			resultHex == "2527da118865db5778a35469bcd6fed31278d34db69895934fce6686f0c76a6c"
	}

	def "JDK SHA3-256 is SHA3"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA3-256")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA3.HASH_256.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes.length == 32
			javaBytes == resultBytes
			resultBytes == [37, 39, -38, 17, -120, 101, -37, 87, 120, -93, 84, 105, -68, -42, -2, -45, 18, 120, -45, 77, -74, -104, -107, -109, 79, -50, 102, -122, -16, -57, 106, 108] as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = BytesToHex.bytesToHex(resultBytes)
		then:
			javaHex == resultHex
			javaHex.length() == resultHex.length()
			resultHex == "2527da118865db5778a35469bcd6fed31278d34db69895934fce6686f0c76a6c"
	}


	def "JDK SHA3-512 is SHA3"() {
		given:
			byte[] bytes = TEST_STRING.getBytes(StandardCharsets.UTF_8)
			MessageDigest digest = MessageDigest.getInstance("SHA3-512")
		when:
			def javaBytes = digest.digest(bytes)
			def resultBytes = SHA3.HASH_512.bytesResult().getHashFromBytes(bytes)
		then:
			javaBytes.length == resultBytes.length
			javaBytes == resultBytes
			resultBytes == [126, -127, -126, 120, 59, 1, 65, 59, -121, -27, 127, -40, -33, -81, 98, -27, 25, -3, -27, 62, 62, -125, 7, -58, 19, -59, 117, 120, -104, 89, -41, 63, 126, 13, -104, -20, 41, -50, -93, 12, -45, 63, 60, -95, 79, -26, 122, -17, -71, -23, 46, -13, -100, -124, 13, 42, -4, -72, 113, 118, 3, 63, 115, -69] as byte[]
		when:
			def javaHex = BytesToHex.bytesToHex(javaBytes)
			def resultHex = SHA3.HASH_512.stringResult().getHashFromString(TEST_STRING)
		then:
			javaHex == resultHex
			javaHex.length() == resultHex.length()
			resultHex == "7e8182783b01413b87e57fd8dfaf62e519fde53e3e8307c613c575789859d73f7e0d98ec29cea30cd33f3ca14fe67aefb9e92ef39c840d2afcb87176033f73bb"
	}

}
