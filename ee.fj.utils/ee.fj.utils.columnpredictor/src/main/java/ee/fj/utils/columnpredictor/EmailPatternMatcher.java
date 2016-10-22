package ee.fj.utils.columnpredictor;

import java.util.regex.Pattern;

public abstract class EmailPatternMatcher extends ColumnPatternMatcher {
	public EmailPatternMatcher() {
		super(Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"));
	}
}
