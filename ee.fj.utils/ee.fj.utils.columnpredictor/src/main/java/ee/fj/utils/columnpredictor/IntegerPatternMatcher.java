package ee.fj.utils.columnpredictor;

import java.util.Objects;
import java.util.regex.Pattern;

public class IntegerPatternMatcher extends ColumnPatternMatcher {
	public static final String DEFAULT_NAME = "integer";
	private static final Pattern PATTERN = Pattern.compile("^[-]?\\d+$");
	public static final IntegerPatternMatcher INSTANCE = new IntegerPatternMatcher(DEFAULT_NAME);
	
	public IntegerPatternMatcher(String id) {
		super(id, PATTERN);
	}

	public static long getAsLong(Object value) {
		Objects.requireNonNull(value, "The argument must be not null!");
		if (!INSTANCE.matches(value)) {
			throw new IllegalArgumentException( value .toString() + " is not long!");
		}
		return Long.valueOf(value.toString());
	}

	public static int getAsInteger(Object value) {
		Objects.requireNonNull(value, "The argument must be not null!");
		if (!INSTANCE.matches(value)) {
			throw new IllegalArgumentException( value .toString() + " is not integer!");
		}
		Long rv = Long.valueOf(value.toString());
		if (rv < Integer.MIN_VALUE || rv > Integer.MAX_VALUE) {
			
		}
		
		return rv.intValue();
	}
}
