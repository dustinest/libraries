package ee.fj.utils.columnpredictor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The purpose of this test is to show more complex columns.
 * The e-mail type appears at fist column 3 times and at last column 4 times.
 * Therefore the column 0 is null and e-mail is at 4th column
 * @author margus
 *
 */
public class ColumnPredictorTest {
	private MatchingResult result;

	@Before
	public void setup() {
		ColumnPredictor predictor = new ColumnPredictor(
				new ColumnMatcher() {
					@Override public String getLabel() { return "name_column"; }
					@Override public boolean matches(Object column) { return "name".equals(column); }
					
				},
				EmailPatternMatcher.INSTANCE,
				UrlPatternMatcher.INSTANCE
		);
		predictor.addRow(new String[]{"uhhuu@ahaa.ee", "name", "other"});
		predictor.addRow(new String[]{"uhhuu@ahaa.ee", "teie", "name"});
		predictor.addRow("uhhuu@ahaa.ee", "you man!", "name", "uhhuu@ahaa.ee");
		predictor.addRow(new String[]{"ONLY US", "http://www.neti.ee", "name", "uhhuu@ahaa.ee"});
		predictor.addRow(new String[]{"ONLY US", "teie", "nimi", "uhhuu@ahaa.ee"});
		predictor.addRow("ONLY US", "you man!", "name", "uhhuu@ahaa.ee");
		result = predictor.calculate();
	}

	@Test
	public void testColumnsCount() {
		Assert.assertEquals(4, result.getColumnsCount());
	}
	
	@Test
	public void testColumnLabels() {
		Assert.assertEquals(100f, result.getProbabilityAt(0, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0, result.getProbabilityAt(0, UrlPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0, result.getProbabilityAt(0, "name_column"), 0);

		Assert.assertEquals(0, result.getProbabilityAt(1, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(50f, result.getProbabilityAt(1, UrlPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(50f, result.getProbabilityAt(1, "name_column"), 0);

		Assert.assertEquals(0, result.getProbabilityAt(2, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(2, UrlPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(100f, result.getProbabilityAt(2, "name_column"), 0);

		Assert.assertEquals(100f, result.getProbabilityAt(3, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(3, UrlPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0, result.getProbabilityAt(3, "name_column"), 0);

		Assert.assertEquals(0f, result.getProbabilityAt(4, EmailPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(4, UrlPatternMatcher.DEFAULT_NAME), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(4, "name_column"), 0);

		for (int i = 0; i < result.getColumnsCount(); i++) {
			Assert.assertEquals(0, result.getProbabilityAt(i, "does not exist"), 0);
		}
	}

	@Test
	public void testColumnClasses() {
		Assert.assertEquals(100f, result.getProbabilityAt(0, EmailPatternMatcher.class), 0);
		Assert.assertEquals(0, result.getProbabilityAt(0, UrlPatternMatcher.class), 0);

		Assert.assertEquals(0, result.getProbabilityAt(1, EmailPatternMatcher.class), 0);
		Assert.assertEquals(50f, result.getProbabilityAt(1, UrlPatternMatcher.class), 0);

		Assert.assertEquals(0, result.getProbabilityAt(2, EmailPatternMatcher.class), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(2, UrlPatternMatcher.class), 0);

		Assert.assertEquals(100f, result.getProbabilityAt(3, EmailPatternMatcher.class), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(3, UrlPatternMatcher.class), 0);

		Assert.assertEquals(0f, result.getProbabilityAt(4, EmailPatternMatcher.class), 0);
		Assert.assertEquals(0f, result.getProbabilityAt(4, UrlPatternMatcher.class), 0);

	}

	@Test
	public void isColumnLabelAt() {
		Assert.assertTrue(result.isColumnAt(0, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(0, UrlPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(0, "name_column"));

		Assert.assertFalse(result.isColumnAt(1, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertTrue(result.isColumnAt(1, UrlPatternMatcher.DEFAULT_NAME));
		Assert.assertTrue(result.isColumnAt(1, "name_column"));

		Assert.assertFalse(result.isColumnAt(2, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(2, UrlPatternMatcher.DEFAULT_NAME));
		Assert.assertTrue(result.isColumnAt(2, "name_column"));

		Assert.assertTrue(result.isColumnAt(3, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(3, UrlPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(3, "name_column"));

		Assert.assertFalse(result.isColumnAt(4, EmailPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(4, UrlPatternMatcher.DEFAULT_NAME));
		Assert.assertFalse(result.isColumnAt(4, "name_column"));

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

		Assert.assertTrue(result.isColumnAt(3, EmailPatternMatcher.class));
		Assert.assertFalse(result.isColumnAt(3, UrlPatternMatcher.class));

		Assert.assertFalse(result.isColumnAt(4, EmailPatternMatcher.class));
		Assert.assertFalse(result.isColumnAt(4, UrlPatternMatcher.class));
	}
}
