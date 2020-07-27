package com.foxjunior.javafx.loader;

import javafx.scene.Node;

public interface LoaderInitializable {
	/**
	 * This is initialization call for the initializable interface
	 * @param node initialized
	 */
	void initialize(Node node);
}
