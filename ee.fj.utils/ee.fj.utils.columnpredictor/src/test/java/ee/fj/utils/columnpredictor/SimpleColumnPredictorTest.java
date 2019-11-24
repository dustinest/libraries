package ee.fj.utils.columnpredictor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleColumnPredictorTest {
	private MatchingResult result;

	@BeforeEach
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

		Assertions.assertEquals(-1, result.getBestResultFor(EmailPatternMatcher.DEFAULT_NAME));
		Assertions.assertEquals(-1, result.getBestResultFor(UrlPatternMatcher.DEFAULT_NAME));
		Assertions.assertEquals(-1, result.getBestResultFor(NumberPatternMatcher.DEFAULT_NAME));
		Assertions.assertEquals(-1, result.getBestResultFor(IntegerPatternMatcher.DEFAULT_NAME));
		Assertions.assertEquals(-1, result.getBestResultFor("does not exist"));
	}


	@Test
	public void testColumnsCount() {
		Assertions.assertEquals(2, result.getColumnsCount());
	}

	@Test
	public void testBestColumnLabels() {
		Assertions.assertEquals(0, result.getBestResultFor(EmailPatternMatcher.DEFAULT_NAME));
		Assertions.assertEquals(1, result.getBestResultFor(UrlPatternMatcher.DEFAULT_NAME));
		Assertions.assertEquals(-1, result.getBestResultFor("does not exist"));
	}

	@Test
	public void testBestColumnClass() {
		Assertions.assertEquals(0, result.getBestResultFor(EmailPatternMatcher.class));
		Assertions.assertEquals(1, result.getBestResultFor(UrlPatternMatcher.class));
	}

	@Test
	public void testColumnLabels() {
		Assertions.assertEquals(100f, result.getProbabilityAt(0, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assertions.assertEquals(0, result.getProbabilityAt(0, UrlPatternMatcher.DEFAULT_NAME), 0);

		Assertions.assertEquals(0, result.getProbabilityAt(1, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assertions.assertEquals(100f, result.getProbabilityAt(1, UrlPatternMatcher.DEFAULT_NAME), 0);

		Assertions.assertEquals(0, result.getProbabilityAt(2, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assertions.assertEquals(0f, result.getProbabilityAt(2, UrlPatternMatcher.DEFAULT_NAME), 0);

		for (int i = 0; i < result.getColumnsCount(); i++) {
			Assertions.assertEquals(0, result.getProbabilityAt(i, "does not exist"), 0);
		}
	}

	@Test
	public void testColumnClasses() {
		Assertions.assertEquals(100f, result.getProbabilityAt(0, EmailPatternMatcher.class), 0);
		Assertions.assertEquals(0, result.getProbabilityAt(0, UrlPatternMatcher.class), 0);

		Assertions.assertEquals(0, result.getProbabilityAt(1, EmailPatternMatcher.class), 0);
		Assertions.assertEquals(100f, result.getProbabilityAt(1, UrlPatternMatcher.class), 0);

		Assertions.assertEquals(0f, result.getProbabilityAt(2, EmailPatternMatcher.class), 0);
		Assertions.assertEquals(0f, result.getProbabilityAt(2, UrlPatternMatcher.class), 0);

	}

	@Test
	public void isColumnLabelAt() {
		Assertions.assertTrue(result.isColumnAt(0, EmailPatternMatcher.DEFAULT_NAME));
		Assertions.assertFalse(result.isColumnAt(0, UrlPatternMatcher.DEFAULT_NAME));

		Assertions.assertFalse(result.isColumnAt(1, EmailPatternMatcher.DEFAULT_NAME));
		Assertions.assertTrue(result.isColumnAt(1, UrlPatternMatcher.DEFAULT_NAME));

		Assertions.assertFalse(result.isColumnAt(2, EmailPatternMatcher.DEFAULT_NAME));
		Assertions.assertFalse(result.isColumnAt(2, UrlPatternMatcher.DEFAULT_NAME));

		for (int i = 0; i < result.getColumnsCount(); i++) {
			Assertions.assertFalse(result.isColumnAt(i, "does not exist"));
		}
	}

	@Test
	public void isColumnClassAt() {
		Assertions.assertTrue(result.isColumnAt(0, EmailPatternMatcher.class));
		Assertions.assertFalse(result.isColumnAt(0, UrlPatternMatcher.class));

		Assertions.assertFalse(result.isColumnAt(1, EmailPatternMatcher.class));
		Assertions.assertTrue(result.isColumnAt(1, UrlPatternMatcher.class));

		Assertions.assertFalse(result.isColumnAt(2, EmailPatternMatcher.class));
		Assertions.assertFalse(result.isColumnAt(2, UrlPatternMatcher.class));
	}
}
