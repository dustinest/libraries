package ee.fj.utils.looseversion;

import java.util.Objects;

public class LooseVersion implements Comparable<CharSequence>, CharSequence {
	private final String versionString;
	private final char[][] version;

	public LooseVersion(String version) {
		Objects.requireNonNull(version, "Version must be set!");
		this.versionString = version;
		String[] versions = version.split("[^\\p{L}0-9']+");
		this.version = new char[versions.length][];
		for (int i = 0; i < this.version.length; i++) {
			this.version[i] = versions[i].toCharArray();
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof CharSequence) {
			return this.compareTo((CharSequence) obj) == 0;
		}
		return false;
	}

	@Override
	public int compareTo(CharSequence obj) {
		return compare(this, obj);
	}

	/**
	 * @param src
	 * @param target
	 * @return true if versions are equal
	 */
	public static boolean equals(CharSequence src, CharSequence target) {
		return compare(src, target) == 0;
	}

	/**
	 * Compare character. If integer then 9 is larger than 0 otherwise b is larger than a. Case is ignored
	 * @param a
	 * @param b
	 * @return interval of compared character
	 */
	private static int compare(char a, char b) {
		if (Character.isDigit(a) && Character.isDigit(b)) {
			return a - b;
		}
		if (Character.isAlphabetic(a) && Character.isUpperCase(a)) { a = Character.toLowerCase(a); }
		if (Character.isAlphabetic(b) && Character.isUpperCase(a)) { b = Character.toLowerCase(a); }
		return b - a;
	}

	/**
	 * @param src
	 * @param target
	 * @return 0 if both versions are equal, if src is larger than target return 1 else return -1
	 */
	public static int compare(CharSequence src, CharSequence target) {
		if (src == target
				|| (src == null && target == null)
				|| (src != null && target != null && src.toString().equals(target.toString()))
				){
			return 0;
		}
		LooseVersion _src = src instanceof LooseVersion ? (LooseVersion)src : new LooseVersion(src.toString());
		LooseVersion _trg = target instanceof LooseVersion ? (LooseVersion)target : new LooseVersion(target.toString());

		int max = _src.version.length < _trg.version.length ? _src.version.length : _trg.version.length;
		for (int i = 0; i < max; i++) {
			int maxI = _src.version[i].length < _trg.version[i].length ? _src.version[i].length : _trg.version[i].length;

			for (int ii = 0; ii < maxI; ii++) {
				int comp = compare(_src.version[i][ii], _trg.version[i][ii]);
				if (comp > 0) {
					return 1;
				} else if (comp < 0) {
					return -1;
				}
			}

			if (_src.version[i].length > _trg.version[i].length) {
				return 1;
			} else if (_src.version[i].length < _trg.version[i].length) {
				return -1;
			}
		}

		if (_src.version.length > _trg.version.length) {
			return 1;
		} else if (_src.version.length < _trg.version.length) {
			return -1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return this.versionString;
	}

	/**
	 * @param version
	 * @return true if this is after version provided
	 */
	public boolean isAfter(CharSequence version) {
		return this.compareTo(version) > 0;
	}

	/**
	 * @param version
	 * @return true if this is before version provided
	 */
	public boolean isBefore(CharSequence version) {
		return this.compareTo(version) < 0;
	}

	@Override
	public int hashCode() {
		return this.versionString.hashCode();
	}

	@Override
	public int length() {
		return this.versionString.length();
	}

	@Override
	public char charAt(int index) {
		return this.versionString.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return this.subSequence(start, end);
	}
}