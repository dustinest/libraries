package com.foxjunior.javafx.validator.number;

import java.text.ParseException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongRangeValidatorTest {
	@Test
	public void testParsingLong() throws ParseException {
		LongRangeValidator validator = new LongRangeValidator(null, "test",
				1, 5, 20);
		Long expect = 1L;
		Long result = validator.parse("1.1123");
		Assertions.assertEquals(expect, result);
	}

	@Test
	public void testIfNull() throws ParseException {
		LongRangeValidator validator = new LongRangeValidator(null, "test",
				1, 5, 20);
		Long expect = null;
		Long result = validator.parse(null);
		Assertions.assertEquals(expect, result);
	}

	@Test
	public void testIfEmty() throws ParseException {
		LongRangeValidator validator = new LongRangeValidator(null, "test",
				1, 5, 20);
		Long expect = null;
		Long result = validator.parse("");
		Assertions.assertEquals(expect, result);
	}

}
