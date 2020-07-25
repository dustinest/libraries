package ee.fj.enums;

import java.util.Optional;
import java.util.function.Function;

public enum ToUpperAndLowerCaseEnum {
	Personal,
	Business;

	static final Function<String, Optional<ToUpperAndLowerCaseEnum>> CASE_INSENSITIVE = QuickSearch.buildCaseInsensitive(ToUpperAndLowerCaseEnum.class);
}
