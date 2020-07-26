package ee.fj.arrays.filter;

import java.util.Collection;
import java.util.Map;

public class ArrayFilter {
	public static boolean listHasValues(Collection<?> list) {
		return list != null && list.size() > 0;
	}

	public static boolean arrayHasValues(Object[] list) {
		return list != null && list.length > 0;
	}

	public static boolean mapHasValues(Map<?, ?> map) {
		return map != null && map.entrySet().size() > 0;
	}
}
