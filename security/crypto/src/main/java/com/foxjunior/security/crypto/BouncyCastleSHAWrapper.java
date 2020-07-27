package com.foxjunior.security.crypto;

import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

class BouncyCastleSHAWrapper implements SHAWrapper {
	private final Class<? extends SHA3.DigestSHA3> digest;

	<T extends SHA3.DigestSHA3> BouncyCastleSHAWrapper(Class<T> digest) {
		this.digest = digest;
		getShaDigestInstance().digest("Lorem Ipsum Est".getBytes(StandardCharsets.UTF_8));
	}

	public SHA3.DigestSHA3 getShaDigestInstance() {
		try {
			return digest.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new UnsupportedOperationException("Can not create new instance of " + digest.getName() + "!", e);
		}
	}
}
