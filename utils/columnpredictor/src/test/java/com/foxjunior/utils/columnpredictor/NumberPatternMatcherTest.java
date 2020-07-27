package com.foxjunior.utils.columnpredictor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberPatternMatcherTest {
	@Test
	public void testInteger() {
		Assertions.assertTrue(NumberPatternMatcher.INSTANCE.matches("1"));
		Assertions.assertTrue(NumberPatternMatcher.INSTANCE.matches("-1"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("+1"));
	}

	@Test
	public void testDotNumber() {
		Assertions.assertTrue(NumberPatternMatcher.INSTANCE.matches("1.1"));
		Assertions.assertTrue(NumberPatternMatcher.INSTANCE.matches("-9.9"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("+1.1"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1."));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("."));
	}

	@Test
	public void testDoubleDotNumber() {
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1.1.1"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1..1"));
	}

	@Test
	public void testCommaNumber() {
		Assertions.assertTrue(NumberPatternMatcher.INSTANCE.matches("1,1"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches(","));
	}

	@Test
	public void testDoubleCommaNumber() {
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,1,1"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,,1"));
	}

	@Test
	public void testMixed() {
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,1.1"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,.1"));
		Assertions.assertFalse(NumberPatternMatcher.INSTANCE.matches("1.,1"));
	}

	@Test
	public void testDoubleCast() {
		Assertions.assertEquals(1d, NumberPatternMatcher.getAsDouble("1"), 0);
		Assertions.assertEquals(1.1d, NumberPatternMatcher.getAsDouble("1.1"), 0);
		Assertions.assertEquals(1.1d, NumberPatternMatcher.getAsDouble("1,1"), 0);
	}

	@Test
	public void testFloatCast() {
		Assertions.assertEquals(1f, NumberPatternMatcher.getAsFloat("1"), 0);
		Assertions.assertEquals(1.1f, NumberPatternMatcher.getAsFloat("1.1"), 0);
		Assertions.assertEquals(1.1f, NumberPatternMatcher.getAsFloat("1,1"), 0);
	}

	@Test
	public void testIntegerCast() {
		Assertions.assertEquals(1, NumberPatternMatcher.getAsInteger("1"), 0);
		Assertions.assertEquals(1, NumberPatternMatcher.getAsInteger("1.1"), 0);
		Assertions.assertEquals(1, NumberPatternMatcher.getAsInteger("1,1"), 0);
	}
}
