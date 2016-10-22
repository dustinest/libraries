package ee.fj.utils.columnpredictor;

import java.util.regex.Pattern;

public abstract class UrlPatternMatcher extends ColumnPatternMatcher {
	public UrlPatternMatcher() {
		super(Pattern.compile("^(http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*$"));
	}
}