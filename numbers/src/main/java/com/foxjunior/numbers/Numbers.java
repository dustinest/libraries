package com.foxjunior.numbers;

import java.util.Optional;

public class Numbers {
	/**
	 * Nullsafe method to find first Number from the string.
	 * @param value to find number from
	 * @return  Long, Double or null if there was no legal number found
	 */
	public static Number getFirstNumberOrNull(String value) {
		if (value == null) return null;
		int length = value.length();
		if (length == 0) return null;
		char[] result = new char[length];
		int count = 0;
		byte commaCount = 0;
		for (int i = 0; i < length; i++) {
			char c = value.charAt(i);
			if (Character.isDigit(c)) {
				if (commaCount > 1) {
					return null;
				}
				result[count++] = c;
			} else if (c == '.' && count > 0) {
				if (commaCount == 0) {
					result[count++] = c;
				}
				commaCount++;
			} else if (count > 0) {
				break;
			}
		}
		if (count == 0) return null;
		String nr = new String(result, 0, count);
		if (commaCount > 0) {
			return Double.valueOf(nr);
		}
		return Long.valueOf(nr);
	}

	/**
	 * Nullsafe method to find first Number from the string.
	 * @param value to find number from
	 * @return Optional of Long, Double or Optional empty if there was no legal number found
	 */
	public static Optional<Number> getFirstNumber(String value) {
		return Optional.ofNullable(getFirstNumberOrNull(value));
	}

	/**
	 * Nullsafe method to find last Number from the string.
	 * @param value to find number from
	 * @return  Long, Double or null if there was no legal number found
	 */
	public static Number getLastNumberOrNull(String value) {
		if (value == null) return null;
		int length = value.length();
		if (length == 0) return null;
		char[] result = new char[length];
		int count = 0;
		byte commaCount = 0;
		for (int i = length -1 ; i >= 0; i--) {
			char c = value.charAt(i);
			if (Character.isDigit(c)) {
				if (commaCount > 1) {
					return null;
				}
				result[length - 1 - (count++)] = c;
			} else if (c == '.' && count > 0) {
				if (commaCount == 0) {
					result[length - 1 - (count++)] = c;
				}
				commaCount++;
			} else if (count > 0) {
				break;
			}
		}
		if (count == 0) return null;
		String nr = new String(result, length-count, count);
		if (commaCount > 0) {
			return Double.valueOf(nr);
		}
		return Long.valueOf(nr);
	}


	/**
	 * Nullsafe method to find first Number from the string.
	 * @param value to find number from
	 * @return Optional of Long, Double or Optional empty if there was no legal number found
	 */
	public static Optional<Number> getLastNumber(String value) {
		return Optional.ofNullable(getLastNumberOrNull(value));
	}
}
