package ee.fj.utils.columnpredictor;

import java.util.regex.Pattern;

public class EmailPatternMatcher extends ColumnPatternMatcher {
	public static final String DEFAULT_NAME = "email";
	private static final Pattern PATTERN = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	public static final EmailPatternMatcher INSTANCE = new EmailPatternMatcher(DEFAULT_NAME);

	public EmailPatternMatcher(String id) {
		super(id, PATTERN);
	}
}
