package com.foxjunior.security.crypto;

public interface SHA {
	DigestSHA<String> stringResult();
	DigestSHA<byte[]> bytesResult();
}
