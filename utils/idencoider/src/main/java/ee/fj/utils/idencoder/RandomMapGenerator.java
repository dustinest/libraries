package ee.fj.utils.idencoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomMapGenerator {
	public enum Properties {
		NUMBERS,
		LOWERCASE,
		UPPERCASE
	}

	private static void addIfNotExists(final List<Character> characters, final int character) {
		char c = (char)character;
		if (characters.contains(c)) return;
		characters.add(c);
	}

	static List<Character> getChars(Properties... properties) {
		if (properties == null || properties.length == 0) throw new IllegalArgumentException("Properties must not be empty!");
		final List<Character> characters = new ArrayList<>();
		for (Properties p : properties) {
			switch (p) {
				case NUMBERS:
					IntStream.rangeClosed('0', '9').forEach(c-> addIfNotExists(characters, c));
					continue;
				case LOWERCASE:
					IntStream.rangeClosed('a', 'z').forEach(c-> addIfNotExists(characters, c));
					continue;
				case UPPERCASE:
					IntStream.rangeClosed('A', 'Z').forEach(c-> addIfNotExists(characters, c));
					continue;
			}
		}
		return characters;
	}

	public static char[][] getRandomCharMatrix(int rows, int cols, int seed, Properties... properties) {
		if (rows < 1) throw new IllegalArgumentException("The rows must be larger than 0!");
		if (cols < 1) throw new IllegalArgumentException("The cols must be larger than 0!");
		final Random random = new Random(seed);

		final char[][] rv = new char[rows][cols];
		final List<Character> characters = Collections.unmodifiableList(getChars(properties));
		final List<Character> usableCharacters = new ArrayList<>(characters.size());
		int index = 0;
		for (int row = 0; row < rows; row ++) {
			usableCharacters.clear();
			usableCharacters.addAll(characters);
			for (int col = 0; col < cols; col ++) {
				if (usableCharacters.size() == 0) {
					throw new IllegalArgumentException("All numbers are gone! Looks like the array should be made larger!");
				} else if (usableCharacters.size() == 1) {
					index = 0;
				} else {
					index = random.nextInt(usableCharacters.size());
				}
				rv[row][col] = usableCharacters.remove(index);
			}
		}
		return rv;
	}
}
