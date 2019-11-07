package ee.fj.utils.columnpredictor;

import org.junit.Assert;
import org.junit.Test;

public class NumberPatternMatcherTest {
	@Test
	public void testInteger() {
		Assert.assertTrue(NumberPatternMatcher.INSTANCE.matches("1"));
		Assert.assertTrue(NumberPatternMatcher.INSTANCE.matches("-1"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("+1"));
	}

	@Test
	public void testDotNumber() {
		Assert.assertTrue(NumberPatternMatcher.INSTANCE.matches("1.1"));
		Assert.assertTrue(NumberPatternMatcher.INSTANCE.matches("-9.9"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("+1.1"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1."));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("."));
	}

	@Test
	public void testDoubleDotNumber() {
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1.1.1"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1..1"));
	}

	@Test
	public void testCommaNumber() {
		Assert.assertTrue(NumberPatternMatcher.INSTANCE.matches("1,1"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches(","));
	}

	@Test
	public void testDoubleCommaNumber() {
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,1,1"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,,1"));
	}

	@Test
	public void testMixed() {
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,1.1"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1,.1"));
		Assert.assertFalse(NumberPatternMatcher.INSTANCE.matches("1.,1"));
	}

	@Test
	public void testDoubleCast() {
		Assert.assertEquals(1d, NumberPatternMatcher.getAsDouble("1"), 0);
		Assert.assertEquals(1.1d, NumberPatternMatcher.getAsDouble("1.1"), 0);
		Assert.assertEquals(1.1d, NumberPatternMatcher.getAsDouble("1,1"), 0);
	}

	@Test
	public void testFloatCast() {
		Assert.assertEquals(1f, NumberPatternMatcher.getAsFloat("1"), 0);
		Assert.assertEquals(1.1f, NumberPatternMatcher.getAsFloat("1.1"), 0);
		Assert.assertEquals(1.1f, NumberPatternMatcher.getAsFloat("1,1"), 0);
	}

	@Test
	public void testIntegerCast() {
		Assert.assertEquals(1, NumberPatternMatcher.getAsInteger("1"), 0);
		Assert.assertEquals(1, NumberPatternMatcher.getAsInteger("1.1"), 0);
		Assert.assertEquals(1, NumberPatternMatcher.getAsInteger("1,1"), 0);
	}
}
