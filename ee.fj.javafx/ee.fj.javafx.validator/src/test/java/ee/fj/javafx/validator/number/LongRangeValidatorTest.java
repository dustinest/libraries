package ee.fj.javafx.validator.number;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

public class LongRangeValidatorTest {
	@Test
	public void testParsingLong() throws ParseException {
		LongRangeValidator validator = new LongRangeValidator(null, "test",
				1, 5, 20);
		Long expect = 1L;
		Long result = validator.parse("1.1123");
		Assert.assertEquals(expect, result);
	}

	@Test
	public void testIfNull() throws ParseException {
		LongRangeValidator validator = new LongRangeValidator(null, "test",
				1, 5, 20);
		Long expect = null;
		Long result = validator.parse(null);
		Assert.assertEquals(expect, result);
	}

	@Test
	public void testIfEmty() throws ParseException {
		LongRangeValidator validator = new LongRangeValidator(null, "test",
				1, 5, 20);
		Long expect = null;
		Long result = validator.parse("");
		Assert.assertEquals(expect, result);
	}

}
