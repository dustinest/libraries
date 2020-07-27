package com.foxjunior.utils.idencoder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumericCharactersMapTest {
	@Test
	void testNumbersMap() {
		NumericCharactersMap map = new NumericCharactersMap("9876543210".toCharArray());
		Assertions.assertEquals('9', map.getMappedCharacter('0'));
		Assertions.assertEquals('0', map.getMappedCharacter('9'));

		Assertions.assertEquals('9', map.getReversedCharacter('0'));
		Assertions.assertEquals('0', map.getReversedCharacter('9'));
	}
}
