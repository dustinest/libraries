package ee.fj.commons.arrays.filters;

public class StringFilter {
	public static boolean isEmpty(CharSequence value) {
		return value == null || value.length() == 0;
	}

	public static boolean isNotEmpty(CharSequence value) {
		return !isEmpty(value);
	}

	public static boolean hasText(CharSequence value) {
		if (value == null) return false;
		int length = value.length();
		if (length == 0) return false;
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasNoText(CharSequence value) {
		return !hasText(value);
	}
}
