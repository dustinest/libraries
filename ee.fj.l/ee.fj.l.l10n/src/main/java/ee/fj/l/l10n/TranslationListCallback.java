package ee.fj.l.l10n;

import java.util.Objects;

@FunctionalInterface
public interface TranslationListCallback {
	public void accept(String key, int index, String value);

    default TranslationListCallback andThen(TranslationListCallback after) {
        Objects.requireNonNull(after);

        return (key, index, value) -> {
            accept(key, index, value);
            after.accept(key, index, value);
        };
    }
}
