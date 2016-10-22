package ee.fj.utils.columnpredictor;

import java.util.regex.Pattern;

public abstract class ColumnPatternMatcher implements ColumnMatcher {
	private final Pattern pattern;
	public ColumnPatternMatcher(Pattern pattern) {
		this.pattern = pattern;
	}
	public ColumnPatternMatcher(String regexp) {
		this(Pattern.compile(regexp));
	}

	@Override
	public boolean matches(Object column) {
		if (column == null || !(column instanceof CharSequence))
			return false;
		return pattern.matcher(column.toString().trim()).matches();
	}
}