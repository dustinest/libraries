package ee.fj.utils.columnpredictor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleColumnPredictorTest {
	private MatchingResult result;

	@Before
	public void setup() {
		ColumnPredictor predictor = new ColumnPredictor(
				EmailPatternMatcher.INSTANCE,
				UrlPatternMatcher.INSTANCE,
				NumberPatternMatcher.INSTANCE,
				IntegerPatternMatcher.INSTANCE
		);
		predictor.addRow("uhhuu@ahaa.ee", 1);
		predictor.addRow("uhhuu@ahaa.ee");
		predictor.addRow("uhhuu@ahaa.ee");
		predictor.addRow("somethins else", "http://www.neti.ee");
		result = predictor.calculate();
	}

	@Test
	public void EmptyTest() {
		ColumnPredictor predictor = new ColumnPredictor(
				EmailPatternMatcher.INSTANCE,
				UrlPatternMatcher.INSTANCE,
				NumberPatternMatcher.INSTANCE,
				IntegerPatternMatcher.INSTANCE
		);
		predictor.addRow("abc", "cde");
		MatchingResult result = predictor.calculate();

		Assert.assertEquals(-1, result.getBestResultFor(EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertEquals(-1, result.getBestResultFor(UrlPatternMatcher.DEFAULT_NAME));
		Assert.assertEquals(-1, result.getBestResultFor(NumberPatternMatcher.DEFAULT_NAME));
		Assert.assertEquals(-1, result.getBestResultFor(IntegerPatternMatcher.DEFAULT_NAME));
		Assert.assertEquals(-1, result.getBestResultFor("does not exist"));
	}


	@Test
	public void testColumnsCount() {
		Assert.assertEquals(2, result.getColumnsCount());
	}

	@Test
	public void testBestColumnLabels() {
		Assert.assertEquals(0, result.getBestResultFor(EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertEquals(1, result.getBestResultFor(UrlPatternMatcher.DEFAULT_NAME));
		Assert.assertEquals(-1, result.getBestResultFor("does not exist"));
	}

	@Test
	public void testBestColumnClass() {
		Assert.assertEquals(0, result.getBestResultFor(EmailPatternMatcher.class));
		Assert.assertEquals(1, result.getBestResultFor(UrlPatternMatcher.class));
	}

	@Test
	public void testColumnLabels() {
		Assert.assertEquals(100f, result.getProbabilityAt(0, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0, result.getProbabilityAt(0, UrlPatternMatcher.DEFAULT_NAME), 0);

		Assert.assertEquals(0, result.getProbabilityAt(1, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(100f, result.getProbabilityAt(1, UrlPatternMatcher.DEFAULT_NAME), 0);

		Assert.assertEquals(0, result.getProbabilityAt(2, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(2, UrlPatternMatcher.DEFAULT_NAME), 0);

		for (int i = 0; i < result.getColumnsCount(); i++) {
			Assert.assertEquals(0, result.getProbabilityAt(i, "does not exist"), 0);
		}
	}

	@Test
	public void testColumnClasses() {
		Assert.assertEquals(100f, result.getProbabilityAt(0, EmailPatternMatcher.class), 0);
		Assert.assertEquals(0, result.getProbabilityAt(0, UrlPatternMatcher.class), 0);

		Assert.assertEquals(0, result.getProbabilityAt(1, EmailPatternMatcher.class), 0);
		Assert.assertEquals(100f, result.getProbabilityAt(1, UrlPatternMatcher.class), 0);

		Assert.assertEquals(0f, result.getProbabilityAt(2, EmailPatternMatcher.class), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(2, UrlPatternMatcher.class), 0);

	}

	@Test
	public void isColumnLabelAt() {
		Assert.assertTrue(result.isColumnAt(0, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(0, UrlPatternMatcher.DEFAULT_NAME));

		Assert.assertFalse(result.isColumnAt(1, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertTrue(result.isColumnAt(1, UrlPatternMatcher.DEFAULT_NAME));

		Assert.assertFalse(result.isColumnAt(2, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(2, UrlPatternMatcher.DEFAULT_NAME));

		for (int i = 0; i < result.getColumnsCount(); i++) {
			Assert.assertFalse(result.isColumnAt(i, "does not exist"));
		}
	}

	@Test
	public void isColumnClassAt() {
		Assert.assertTrue(result.isColumnAt(0, EmailPatternMatcher.class));
		Assert.assertFalse(result.isColumnAt(0, UrlPatternMatcher.class));

		Assert.assertFalse(result.isColumnAt(1, EmailPatternMatcher.class));
		Assert.assertTrue(result.isColumnAt(1, UrlPatternMatcher.class));

		Assert.assertFalse(result.isColumnAt(2, EmailPatternMatcher.class));
		Assert.assertFalse(result.isColumnAt(2, UrlPatternMatcher.class));
	}
}
