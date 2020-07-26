package ee.fj.utils.idencoder;

public class NumericCharactersMap {
	private final char[] characters;
	private final Character[] reverted;
	private final int min;

	public NumericCharactersMap(char[] characters) {
		this.characters = characters;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (char character : characters) {
			min = Math.min(character, min);
			max = Math.max(character, max);
		}
		this.min = min;

		this.reverted = new Character[max - min + 1];
		for (int i = 0; i < characters.length; i++) {
			this.reverted[characters[i] - min] = Character.forDigit(i, 10);
		}
	}

	public char getMappedCharacter(char character) {
		return characters[Character.getNumericValue(character)];
	}

	public char getReversedCharacter(char character) {
		return reverted[character - min];
	}
}
