package com.foxjunior.commons.filters;

public class ArrayFilter {
	public static boolean isEmpty(Object[] list) {
		return list == null || list.length > 0;
	}

	public static boolean notEmpty(Object[] list) {
		return (!isEmpty(list));
	}
}
