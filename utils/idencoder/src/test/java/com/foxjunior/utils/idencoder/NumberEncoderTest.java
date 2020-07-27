package com.foxjunior.utils.idencoder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NumberEncoderTest {
	private static final char[] NUMBER_TO_CHAR_ARRAY =  "0123456789".toCharArray();
	private static final char[][] CHAR_MATRIX = new char[10][];
	static {
		for (int row = 0; row < 10; row++) {
			CHAR_MATRIX[row] = NUMBER_TO_CHAR_ARRAY;
		}
	}
	@Test
	void primitiveFillLengthTest() {
		NumberEncoder numberEncoder = new NumberEncoder(true, CHAR_MATRIX, NUMBER_TO_CHAR_ARRAY);

		char[] result = new char[6];
		int position = numberEncoder.fillLength(result, 0, 4, 2);
		Assertions.assertEquals(2, position);
		position = numberEncoder.fillLength(result, position, 123, 3);
		Assertions.assertEquals(6, position);

		Assertions.assertEquals("143123",String.valueOf(result));
	}

	@Test
	void primitiveEncodeTest() {
		NumberEncoder numberEncoder = new NumberEncoder(true, CHAR_MATRIX, NUMBER_TO_CHAR_ARRAY);

		String result = numberEncoder.encode(2,4,8);
		Assertions.assertEquals("211241148118", result);
		List<Long> numbers = numberEncoder.decode(result);
		Assertions.assertEquals(3, numbers.size());

		Assertions.assertEquals(2L, numbers.get(0));
		Assertions.assertEquals(4L, numbers.get(1));
		Assertions.assertEquals(8L, numbers.get(2));

		result = numberEncoder.encode(Long.MAX_VALUE, 15);
		Assertions.assertEquals("9219922337203685477580711215", result);
		numbers = numberEncoder.decode(result);

		Assertions.assertEquals(2, numbers.size());
		Assertions.assertEquals(Long.MAX_VALUE, numbers.get(0));
		Assertions.assertEquals(15L, numbers.get(1));
	}

	@Test
	void encodeTest() {
		NumberEncoder numberEncoder = new NumberEncoder(1, RandomMapGenerator.Properties.LOWERCASE);

		String result = numberEncoder.encode(2,4,8);
		Assertions.assertEquals("hiiyykkslnns", result);
		List<Long> numbers = numberEncoder.decode(result);
		Assertions.assertEquals(3, numbers.size());

		Assertions.assertEquals(2L, numbers.get(0));
		Assertions.assertEquals(4L, numbers.get(1));
		Assertions.assertEquals(8L, numbers.get(2));

		result = numberEncoder.encode(Long.MAX_VALUE, 15);
		Assertions.assertEquals("vqbddqqoonqjocmgvnngmjnnynyu", result);
		numbers = numberEncoder.decode(result);

		Assertions.assertEquals(2, numbers.size());
		Assertions.assertEquals(Long.MAX_VALUE, numbers.get(0));
		Assertions.assertEquals(15L, numbers.get(1));

	}


	@Test
	void fillLengthTest() {
		NumberEncoder numberEncoder = new NumberEncoder(1, RandomMapGenerator.Properties.LOWERCASE);

		char[] result = new char[6];

		int position = numberEncoder.fillLength(result, 0, 4, 2);
		Assertions.assertEquals(2, position);
		position = numberEncoder.fillLength(result, position, 123, 3);
		Assertions.assertEquals(6, position);

		Assertions.assertEquals("ixdead",String.valueOf(result));
	}

	@Test
	void fillCharArray() {
		NumberEncoder numberEncoder = new NumberEncoder(1, RandomMapGenerator.Properties.LOWERCASE);

		char[] result = new char[28];
		int index = numberEncoder.fillCharArray("1234567890", result, 0);
		index = numberEncoder.fillCharArray("0987654321", result, index);

		Assertions.assertEquals(28, index);
		Assertions.assertEquals("nnyryndcuptivrrhnrrvlutqyohn",String.valueOf(result));
	}
}
