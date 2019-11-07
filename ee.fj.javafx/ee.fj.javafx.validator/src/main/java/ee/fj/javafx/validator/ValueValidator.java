package ee.fj.javafx.validator;

import java.text.ParseException;

import javafx.beans.value.ChangeListener;

public interface ValueValidator<T> extends Validatable {
	T getValue();
	void setValue(T value);

	/**
	 * @param listener changes if object changes its valid state
	 */
	void addValueListener(ChangeListener<T> listener);

	String format(T value) throws IllegalArgumentException;
	T parse(String value) throws ParseException ;
}
