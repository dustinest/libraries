package ee.fj.javafx.loader;

/**
 * The purpose of this interface is to implement controller with this interface.
 * CompoundLoader can recognize and interact with this
 * @author margus
 *
 */
public interface Loadable {
	void addListener(Runnable isLoaded);
}
