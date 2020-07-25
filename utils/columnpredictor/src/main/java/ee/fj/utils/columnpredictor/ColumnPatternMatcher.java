package ee.fj.utils.columnpredictor;

import java.util.Objects;
import java.util.regex.Pattern;

public abstract class ColumnPatternMatcher implements ColumnMatcher {
	private final Pattern pattern;
	private final String id;

	public ColumnPatternMatcher(String id, Pattern pattern) {
		Objects.requireNonNull(id, "Id must not be null!");
		Objects.requireNonNull(pattern, "the pattern must not be null!");
		this.id = id;
		this.pattern = pattern;
	}

	public ColumnPatternMatcher(String id, String regexp) {
		this(id, Pattern.compile(regexp));
	}

	@Override
	public String getLabel() {
		return id;
	}

	@Override
	public String toString() {
		return this.id + ": " + super.toString();
	}
	
	@Override
	public boolean matches(Object column) {
		if (!(column instanceof CharSequence))
			return false;
		return pattern.matcher(column.toString().trim()).matches();
	}
}