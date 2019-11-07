package ee.fj.utils.columnpredictor;

import org.junit.Assert;
import org.junit.Test;

public class DecimalPatternMatcherTest {
	@Test
	public void testInteger() {
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("-1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("+1"));
	}

	@Test
	public void testDotNumber() {
		Assert.assertTrue(DecimalPatternMatcher.INSTANCE.matches("1.1"));
		Assert.assertTrue(DecimalPatternMatcher.INSTANCE.matches("-9.9"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("+1.1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1."));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("."));
	}

	@Test
	public void testDoubleDotNumber() {
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1.1.1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1..1"));
	}

	@Test
	public void testCommaNumber() {
		Assert.assertTrue(DecimalPatternMatcher.INSTANCE.matches("1,1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches(","));
	}

	@Test
	public void testDoubleCommaNumber() {
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,1,1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,,1"));
	}

	@Test
	public void testMixed() {
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,1.1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1,.1"));
		Assert.assertFalse(DecimalPatternMatcher.INSTANCE.matches("1.,1"));
	}

	@Test
	public void testDoubleCast() {
		ExpectException.testException(IllegalArgumentException.class, () -> DecimalPatternMatcher.getAsDouble("1"));
		Assert.assertEquals(1.1d, DecimalPatternMatcher.getAsDouble("1.1"), 0);
		Assert.assertEquals(1.1d, DecimalPatternMatcher.getAsDouble("1,1"), 0);
	}

	@Test
	public void testFloatCast() {
		ExpectException.testException(IllegalArgumentException.class, () -> DecimalPatternMatcher.getAsFloat("1"));
		Assert.assertEquals(1.1f, DecimalPatternMatcher.getAsFloat("1.1"), 0);
		Assert.assertEquals(1.1f, DecimalPatternMatcher.getAsFloat("1,1"), 0);
	}
}
