package com.foxjunior.commons.filters;

import java.util.Collection;

public class ListFilter {
	public static boolean notEmpty(Collection<?> list) {
		return (!isEmpty(list));
	}

	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.size() == 0;
	}
}
