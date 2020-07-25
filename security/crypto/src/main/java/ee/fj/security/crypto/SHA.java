package ee.fj.security.crypto;

public interface SHA {
	DigestSHA<String> stringResult();
	DigestSHA<byte[]> bytesResult();
}
