package ee.fj.utils.columnpredictor;

import org.junit.Assert;
import org.junit.Test;

import ee.fj.utils.columnpredictor.ColumnMatcher;
import ee.fj.utils.columnpredictor.ColumnPredictor;
import ee.fj.utils.columnpredictor.EmailPatternMatcher;
import ee.fj.utils.columnpredictor.MatchingResult;
import ee.fj.utils.columnpredictor.UrlPatternMatcher;

public class ColumnPredictorTest {
	@Test
	public void testSimpleColumns() {
		//ColumnMatcher<String>[] matchers = new ColumnMatcher<>[1];

		ColumnPredictor predictor = new ColumnPredictor(
			new ColumnMatcher() {
				@Override public String getId() { return "mina"; }
				@Override public boolean matches(Object column) { return "nimi".equals(column); }
				
			},
			new EmailPatternMatcher() {
				@Override public String getId() { return "email"; }
			},
			new UrlPatternMatcher() {
				@Override public String getId() { return "url"; }
			}
		);
		predictor.addColumns(new String[]{"uhhuu@ahaa.ee", "nimi", "tema"});
		predictor.addColumns(new String[]{"uhhuu@ahaa.ee", "teie", "nimi"});
		predictor.addColumns(new String[]{"uhhuu@ahaa.ee", "teie", "nimi", "uhhuu@ahaa.ee"});
		predictor.addColumns(new String[]{"meie", "http://www.neti.ee", "nimi", "uhhuu@ahaa.ee"});
		predictor.addColumns(new String[]{"meie", "teie", "nimi", "uhhuu@ahaa.ee"});
		predictor.addColumns(new String[]{"meie", "teie", "nimi", "uhhuu@ahaa.ee"});
		MatchingResult result = predictor.calculate();
		Assert.assertEquals(2, result.getColumn("mina"));
		Assert.assertEquals(3, result.getColumn("email"));
		Assert.assertEquals(1, result.getColumn("url"));
		
		
	}
}
