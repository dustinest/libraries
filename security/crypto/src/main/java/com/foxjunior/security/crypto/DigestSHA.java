package com.foxjunior.security.crypto;

public interface DigestSHA<T> {
	T getHashFromString(final CharSequence charSequence, final CharSequence... others);
	T getHashFromBytes(final byte[] bytes);
}
