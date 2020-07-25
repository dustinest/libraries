package ee.fj.utils.idencoder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class RandomMapGeneratorTest {
	@Test
	void numbers() {
		List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.NUMBERS);
		Assertions.assertEquals(10, result.size());
		String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining());
		Assertions.assertEquals("0123456789", stringResult);
	}

	@Test
	void lowercase() {
		List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.LOWERCASE);
		Assertions.assertEquals(26, result.size());
		String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining());
		Assertions.assertEquals("abcdefghijklmnopqrstuvwxyz", stringResult);
	}

	@Test
	void uppercase() {
		List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.UPPERCASE);
		Assertions.assertEquals(26, result.size());
		String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining());
		Assertions.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", stringResult);
	}

	@Test
	void lowerAndUppercase() {
		List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.UPPERCASE, RandomMapGenerator.Properties.LOWERCASE);
		Assertions.assertEquals(52, result.size());
		String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining());
		Assertions.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", stringResult);
	}

	@Test
	void allValues() {
		List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.values());
		String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining());
		Assertions.assertEquals("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", stringResult);
	}

	@Test
	void mixedDuplicates() {
		List<Character> result = RandomMapGenerator.getChars(
				RandomMapGenerator.Properties.NUMBERS, RandomMapGenerator.Properties.NUMBERS,
				RandomMapGenerator.Properties.LOWERCASE,
				RandomMapGenerator.Properties.UPPERCASE,
				RandomMapGenerator.Properties.LOWERCASE
		);
		String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining());
		Assertions.assertEquals("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", stringResult);
	}

	@Test
	void throwExceptionWhenEmpty() {
		Assertions.assertThrows(IllegalArgumentException.class, RandomMapGenerator::getChars);
	}

	@Test
	void oneRowRandomCharMatrix() {
		char[][] chars = RandomMapGenerator.getRandomCharMatrix(1, 10, 1, RandomMapGenerator.Properties.LOWERCASE);
		Assertions.assertEquals(1, chars.length);
		Assertions.assertEquals(10, chars[0].length);
		char[] controlChars = new char[]{'r','n','h','o','y','q','t','u','l','v'};
		for (int i = 0; i < chars[0].length; i++) {
			Assertions.assertEquals(controlChars[i], chars[0][i]);
		}
	}

	@Test
	void tenRowsRandomCharMatrix() {
		char[][] chars = RandomMapGenerator.getRandomCharMatrix(10, 10, 2, RandomMapGenerator.Properties.LOWERCASE);
		Assertions.assertEquals(10, chars.length);
		char[][] controlChars = new char[][]{
				{'s','x','i','k','l','t','g','n','j','q',},
				{'c','m','l','d','q','k','j','r','e','f',},
				{'w','x','z','n','p','a','d','l','m','f',},
				{'r','b','i','h','n','g','x','f','m','d',},
				{'k','f','q','y','o','t','s','r','m','z',},
				{'x','q','k','e','w','p','y','n','j','h',},
				{'e','n','y','c','v','o','x','h','w','r',},
				{'f','m','d','t','h','n','l','j','x','z',},
				{'z','s','m','e','q','t','j','p','g','n',},
				{'e','l','r','c','x','q','n','d','p','m',}
		};
		for (int row = 0; row < chars.length; row++) {
			Assertions.assertEquals(10, chars[row].length);
			for (int col = 0; col < chars[row].length; col++) {
				Assertions.assertEquals(controlChars[row][col], chars[row][col], "Value at chars[" + row + "][" + col + "]");
			}
		}
	}
}
