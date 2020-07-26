package ee.fj.commons.arrays.filters;

import java.util.Map;

public class MapFilter {
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.size() == 0;
	}

	public static boolean notEmpty(Map<?, ?> map) {
		return (!isEmpty(map));
	}
}
