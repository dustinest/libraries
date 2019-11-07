package ee.fj.javafx.validator;

import javafx.beans.value.ChangeListener;

/**
 * Base validatable interface
 * @author Margus Rebane
 */
public interface Validatable {
	/**
	 * @return true if object is valid
	 */
	boolean isValid();
	/**
	 * @param listener changes if object changes its valid state
	 */
	void addValidatableListener(ChangeListener<? super Boolean> listener);
}
