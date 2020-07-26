package ee.fj.strings.utils;

import java.util.Objects;
import java.util.Optional;

public class Strings {
	private static String trimAndReturnNullIfEmpty(String value) {
		int endIndex = value.length() - 1;
		if (endIndex < 0) return null;
		int beginIndex = 0;
		while (beginIndex <= endIndex && Character.isWhitespace(value.charAt(beginIndex))) {
			beginIndex++;
		}
		while (endIndex > beginIndex && Character.isWhitespace(value.charAt(endIndex))) {
			endIndex--;
		}
		if (beginIndex >= endIndex + 1) return null;
		return value.substring(beginIndex, endIndex + 1);
	}

	/**
	 * Nullsafe and quicker method for conventional trimming
	 * Trim whitespace from start end end
	 * @param value to trim the whitespace
	 * @return trimmed value
	 */
	public static String trim(String value) {
		if (Objects.isNull(value)) return null;
		String result = trimAndReturnNullIfEmpty(value);
		if (result == null) return "";
		return result;
	}

	/**
	 * Quicker method for conventional trimming
	 * Trim whitespace from start end end
	 * @param value to trim the whitespace
	 * @return Optional empty if no text otherwise Optional value of trimmed string
	 */
	public static Optional<String> trimIfHasText(String value) {
		if (Objects.isNull(value)) return Optional.empty();
		return Optional.ofNullable(trimAndReturnNullIfEmpty(value));
	}

	private static String trimAllAndReturnNullIfEmpty(String value) {
		int length = value.length();
		if (length == 0) return null;
		char[] result = new char[length];
		int count = 0;
		for (int i = 0; i < length; i++) {
			char c = value.charAt(i);
			if (!Character.isWhitespace(c)) {
				result[count++] = c;
			}
		}
		if (count == 0) return null;
		return new String(result, 0, count);
	}

	/**
	 * Nullsafe method to remove all whitespace from the string
	 * @param value to trim
	 * @return null if value is null, empty string if no value or else string
	 */
	public static String removeWhitespace(String value) {
		if (Objects.isNull(value)) return null;
		String result = trimAllAndReturnNullIfEmpty(value);
		if (result == null) return "";
		return result;
	}

	/**
	 * Nullsafe method to remove all whitespace from the string
	 * @param value to trim
	 * @return empty optional if value is null or empty, otherwise optional value of string
	 */
	public static Optional<String> removeWhitespaceIfHasText(String value) {
		if (value == null) return Optional.empty();
		return Optional.ofNullable(trimAllAndReturnNullIfEmpty(value));
	}
}
