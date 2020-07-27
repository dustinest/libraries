package com.foxjunior.utils.columnpredictor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DecimalPatternMatcherTest {
	@Test
	public void testInteger() {
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("-1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("+1"));
	}

	@Test
	public void testDotNumber() {
		Assertions.assertTrue(DecimalPatternMatcher.INSTANCE.matches("1.1"));
		Assertions.assertTrue(DecimalPatternMatcher.INSTANCE.matches("-9.9"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("+1.1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1."));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("."));
	}

	@Test
	public void testDoubleDotNumber() {
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1.1.1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1..1"));
	}

	@Test
	public void testCommaNumber() {
		Assertions.assertTrue(DecimalPatternMatcher.INSTANCE.matches("1,1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches(","));
	}

	@Test
	public void testDoubleCommaNumber() {
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,1,1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,,1"));
	}

	@Test
	public void testMixed() {
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,1.1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,.1"));
		Assertions.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1.,1"));
	}

	@Test
	public void testDoubleCast() {
		ExpectException.testException(IllegalArgumentException.class, () -> DecimalPatternMatcher.getAsDouble("1"));
		Assertions.assertEquals(1.1d, DecimalPatternMatcher.getAsDouble("1.1"), 0);
		Assertions.assertEquals(1.1d, DecimalPatternMatcher.getAsDouble("1,1"), 0);
	}

	@Test
	public void testFloatCast() {
		ExpectException.testException(IllegalArgumentException.class, () -> DecimalPatternMatcher.getAsFloat("1"));
		Assertions.assertEquals(1.1f, DecimalPatternMatcher.getAsFloat("1.1"), 0);
		Assertions.assertEquals(1.1f, DecimalPatternMatcher.getAsFloat("1,1"), 0);
	}
}
