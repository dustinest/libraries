package ee.fj.utils.columnpredictor;

import java.util.regex.Pattern;

public class UrlPatternMatcher extends ColumnPatternMatcher {
	public static final String DEFAULT_NAME = "url";
	private static final Pattern PATTERN = Pattern.compile("^(http|https|ftp)://[a-zA-Z0-9\\-.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-._?,'/\\\\+&amp;%$#=~])*$");
	public static final UrlPatternMatcher INSTANCE = new UrlPatternMatcher(DEFAULT_NAME);

	public UrlPatternMatcher(String id) {
		super(id, PATTERN);
	}
}
