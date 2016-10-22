package ee.fj.javafx.loader;

import java.util.Arrays;
import java.util.Collection;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Useful to control if panels are loaded
 * @author margus
 *
 */
public class CompoundLoader {
	public static void load(LoaderCallback onLoad, Object... initializables) {
		loadArray(onLoad, Arrays.asList(initializables));
	}

	public static void loadArray(LoaderCallback onLoad, Collection<Object> initializables) {
		final IntegerProperty currentValue = new SimpleIntegerProperty(0);
		int totalAmmount = initializables.size();
		for (Object v : initializables) {
			if (v instanceof Loadable) {
				((Loadable)v).addListener(() -> {
					currentValue.set(currentValue.get() + 1);
					onLoad.loaded(totalAmmount, currentValue.get());
				});
			} else FXMLLoaderFactory.load(v, onload -> {
				currentValue.set(currentValue.get() + 1);
				onLoad.loaded(totalAmmount, currentValue.get());
			});
		}
	}
}
