package ee.fj.security.crypto;

public enum SHA3 implements SHA {
	HASH_224(org.bouncycastle.jcajce.provider.digest.SHA3.Digest224.class, "SHA3-224"),
	HASH_256(org.bouncycastle.jcajce.provider.digest.SHA3.Digest256.class, "SHA3-256"),
	HASH_384(org.bouncycastle.jcajce.provider.digest.SHA3.Digest384.class, "SHA3-384"),
	HASH_512(org.bouncycastle.jcajce.provider.digest.SHA3.Digest512.class, "SHA3-512");

	private final DigestSHA<String> stringDigestSHA;
	private final DigestSHA<byte[]> byteDigestSha;

	<T extends org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3> SHA3(Class<T> bouncyCastleClazz, String jdkAlgorithm) {
		DigestSHA<byte[]> SHADigester;
		try {
			SHADigester = new JDKSHAWrapper(jdkAlgorithm);
		} catch (UnsupportedOperationException e) {
			SHADigester = new BouncyCastleSHAWrapper(bouncyCastleClazz);
		}
		this.byteDigestSha = SHADigester;
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
