package com.foxjunior.security.crypto;

public class BytesToHex {
	static String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte aByte : bytes) {
			String hex = Integer.toHexString(0xff & aByte);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
