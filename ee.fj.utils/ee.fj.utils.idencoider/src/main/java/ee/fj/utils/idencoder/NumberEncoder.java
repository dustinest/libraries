package ee.fj.utils.idencoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberEncoder {
	private final NumericCharactersMap[] charMatrix;
	private final boolean firstCharAsIndicator;
	private final NumericCharactersMap numberToChar;

	public NumberEncoder(boolean firstCharAsIndicator, char[][] charMatrix, char[] numberToChar) {
		this.charMatrix = new NumericCharactersMap[10];
		for (int i = 0; i < charMatrix.length; i++) {
			this.charMatrix[i] = new NumericCharactersMap(charMatrix[i]);
		}
		this.numberToChar = new NumericCharactersMap(numberToChar);

		this.firstCharAsIndicator = firstCharAsIndicator;
	}

	public NumberEncoder(boolean firstCharAsIndicator, int seed, RandomMapGenerator.Properties... properties) {
		this(firstCharAsIndicator,
				RandomMapGenerator.getRandomCharMatrix(10, 10, seed, properties),
				RandomMapGenerator.getRandomCharMatrix(1, 10, seed, properties)[0]);
	}

	public NumberEncoder(int seed, RandomMapGenerator.Properties... properties) {
		this(true, seed, properties);
	}

	int fillLength(final char[] chars, final int startIndex, final int length, int row) {
		char[] stringLength = String.valueOf(length).toCharArray();
		String stringLengthLength = String.valueOf(stringLength.length);
		if (stringLengthLength.length() > 9) throw new IllegalArgumentException("String length must be less than 10!");

		chars[startIndex] = charMatrix[row].getMappedCharacter(stringLengthLength.charAt(0));
		int index = startIndex + 1;
		for (int i = 0; i < stringLength.length; i++) {
			chars[index] = charMatrix[row].getMappedCharacter(stringLength[i]);
			index++;
		}
		return index;
	}

	int fillCharArray(CharSequence value, char[] chars, int startIndex) {
		int length = value.length();
		if(length == 0) throw new IllegalArgumentException("The value should not be empty!");

		char rowIndexChar = firstCharAsIndicator ? value.charAt(0) : value.charAt(length - 1);
		int row = Character.getNumericValue(rowIndexChar);
		chars[startIndex] = numberToChar.getMappedCharacter(rowIndexChar);
		int index = fillLength(chars, startIndex + 1, length, row);


		for (int i = 0; i < length; i++) {
			chars[index] = charMatrix[row].getMappedCharacter(value.charAt(i));
			index++;
		}
		return index;
	}

	public String encode(Number first, Number... others) {
		StringBuilder stringBuilder = new StringBuilder();
		char[] buffer = new char[23]; // long maximum + row + length length + length

		int index = fillCharArray(first.toString(), buffer, 0);
		stringBuilder.append(buffer, 0, index);

		for (Number n : others) {
			index = fillCharArray(n.toString(), buffer, 0);
			stringBuilder.append(buffer, 0, index);
		}

		return stringBuilder.toString();
	}

	public List<Long> decode(final String value) {
		final char[] chars = value.toCharArray();

		List<Long> result = new ArrayList<>();

		int row = -1;
		int lengthLength = -1;
		int length = -1;

		char[] buffer = new char[19];
		StringBuilder sb = new StringBuilder(19);

		int bufferIndex = 0;
		for (int valueIndex = 0 ; valueIndex < chars.length; valueIndex++) {
			row = Character.getNumericValue(numberToChar.getReversedCharacter(chars[valueIndex++]));
			lengthLength = Character.getNumericValue(charMatrix[row].getReversedCharacter(chars[valueIndex++]));

			for (bufferIndex = 0; bufferIndex < lengthLength; bufferIndex++) {
				buffer[bufferIndex] = charMatrix[row].getReversedCharacter(chars[valueIndex++]);
			}
			sb.setLength(0);
			sb.append(buffer, 0, lengthLength);
			length = Integer.parseInt(sb.toString());

			for (bufferIndex = 0; bufferIndex < length; bufferIndex++) {
				buffer[bufferIndex] = charMatrix[row].getReversedCharacter(chars[valueIndex++]);
			}
			sb.setLength(0);
			sb.append(buffer, 0, length);
			result.add(Long.parseLong(sb.toString()));

			valueIndex--; // next iteration increases the number!
		}
		return result;
	}

}
