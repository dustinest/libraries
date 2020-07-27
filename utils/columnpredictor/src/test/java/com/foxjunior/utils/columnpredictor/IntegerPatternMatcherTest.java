package com.foxjunior.utils.columnpredictor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerPatternMatcherTest {
	@Test
	public void testInteger() {
		Assertions.assertTrue(IntegerPatternMatcher.INSTANCE.matches("1"));
		Assertions.assertTrue(IntegerPatternMatcher.INSTANCE.matches("-1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("+1"));
	}

	@Test
	public void testDotNumber() {
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1.1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("-9.9"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("+1.1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1."));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("."));
	}

	@Test
	public void testDoubleDotNumber() {
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1.1.1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1..1"));
	}

	@Test
	public void testCommaNumber() {
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches(","));
	}

	@Test
	public void testDoubleCommaNumber() {
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,1,1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,,1"));
	}

	@Test
	public void testMixed() {
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,1.1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,.1"));
		Assertions.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1.,1"));
	}

	@Test
	public void testIntegerCast() {
		Assertions.assertEquals(1, IntegerPatternMatcher.getAsInteger("1"), 0);
		ExpectException.testException(IllegalArgumentException.class, () -> IntegerPatternMatcher.getAsInteger("1.1"));
	}
}
