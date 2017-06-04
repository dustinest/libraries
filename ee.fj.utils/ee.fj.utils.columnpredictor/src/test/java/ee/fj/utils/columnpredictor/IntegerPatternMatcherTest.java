package ee.fj.utils.columnpredictor;

import org.junit.Assert;
import org.junit.Test;

public class IntegerPatternMatcherTest {
	@Test
	public void testInteger() {
		Assert.assertTrue(IntegerPatternMatcher.INSTANCE.matches("1"));
		Assert.assertTrue(IntegerPatternMatcher.INSTANCE.matches("-1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("+1"));
	}

	@Test
	public void testDotNumber() {
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1.1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("-9.9"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("+1.1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1."));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("."));
	}

	@Test
	public void testDoubleDotNumber() {
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1.1.1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1..1"));
	}

	@Test
	public void testCommaNumber() {
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches(","));
	}

	@Test
	public void testDoubleCommaNumber() {
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,1,1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,,1"));
	}

	@Test
	public void testMixed() {
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,1.1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1,.1"));
		Assert.assertFalse(IntegerPatternMatcher.INSTANCE.matches("1.,1"));
	}

	@Test
	public void testIntegerCast() {
		Assert.assertEquals(1, IntegerPatternMatcher.getAsInteger("1"), 0);
		ExpectException.testException(IllegalArgumentException.class, () -> IntegerPatternMatcher.getAsInteger("1.1"));
	}
}
