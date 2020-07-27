package com.foxjunior.security.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class JDKSHAWrapper implements SHAWrapper {
	private final String algorithm;

	JDKSHAWrapper(String algorithm) {
		this.algorithm = algorithm;
		getShaDigestInstance();
	}

	public MessageDigest getShaDigestInstance() {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException("Can not create new instance of " + algorithm + "!", e);
		}
	}
}
