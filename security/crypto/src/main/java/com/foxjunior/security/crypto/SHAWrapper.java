package com.foxjunior.security.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

interface SHAWrapper extends DigestSHA<byte[]> {
	MessageDigest getShaDigestInstance();

	@Override
	default byte[] getHashFromString(final CharSequence charSequence, final CharSequence... others) {
		MessageDigest messageDigest = getShaDigestInstance();
		if (charSequence == null || charSequence.length() == 0) throw new IllegalArgumentException("The digested string must have a value!");
		messageDigest.update(charSequence.toString().getBytes(StandardCharsets.UTF_8));

		for (CharSequence o : others) {
			if (o == null || o.length() == 0) throw new IllegalArgumentException("The digested string must have a value!");
			messageDigest.update(o.toString().getBytes(StandardCharsets.UTF_8));
		}
		return messageDigest.digest();
	}

	@Override
	default byte[] getHashFromBytes(final byte[] bytes) {
		Objects.requireNonNull(bytes, "Provided bytes must not be null!");
		if (bytes.length == 0) throw new IllegalArgumentException("Provied bytes must not be empty!");
		return getShaDigestInstance().digest(bytes);
	}
}
