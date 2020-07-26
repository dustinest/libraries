package ee.fj.security.crypto;

public enum SHA2 implements SHA {
	HASH_224("SHA-224"),
	HASH_256("SHA-256"),
	HASH_384("SHA-384"),
	HASH_512("SHA-512");

	private final DigestSHA<String> stringDigestSHA;
	private final DigestSHA<byte[]> byteDigestSha;

	SHA2(String jdkAlgorithm) {
		this.byteDigestSha = new JDKSHAWrapper(jdkAlgorithm);
		stringDigestSHA = new DigestSHA<>() {
			@Override
			public String getHashFromString(CharSequence charSequence, CharSequence... others) {
				return BytesToHex.bytesToHex(byteDigestSha.getHashFromString(charSequence, others));
			}

			@Override
			public String getHashFromBytes(byte[] bytes) {
				return BytesToHex.bytesToHex(byteDigestSha.getHashFromBytes(bytes));
			}
		};
	}

	@Override
	public DigestSHA<String> stringResult() {
		return stringDigestSHA;
	}

	@Override
	public DigestSHA<byte[]> bytesResult() {
		return byteDigestSha;
	}
}
