package com.foxjunior.utils.columnpredictor;

public interface ColumnMatcher {
	boolean matches(Object column);
	String getLabel();
}
