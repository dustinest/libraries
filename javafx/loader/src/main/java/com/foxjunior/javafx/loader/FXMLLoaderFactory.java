package com.foxjunior.javafx.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;

public class FXMLLoaderFactory<T> {
	@SuppressWarnings("FieldCanBeLocal")
	private final String RESOURCE_PATH = "/fxml/%s.fxml";
	private static final Logger LOGGER = Logger.getLogger(FXMLLoaderFactory.class.getName());

	protected FXMLLoaderFactory(T controller, Consumer<T> onInitialized) {
		boolean setController = true;
		boolean setRoot = true;
		String fileName = null;
//		System.out.println(controller.toString());
		if (controller.getClass().isAnnotationPresent(FXMLLoader.class)) {
			FXMLLoader annotation = (FXMLLoader)controller.getClass().getAnnotation(FXMLLoader.class);
			setController = annotation.controller();
			setRoot = annotation.root();
			fileName = annotation.fileName();
		}

		if (fileName == null || fileName.equals("")) {
			fileName = getFileName(controller);
			//System.out.println("NEW file name:" + fileName);
		}

		URL template = controller.getClass().getResource(fileName);
/*
		System.out.println(fileName);
		System.out.println(template);
*/
		if (template == null) {
			throw new IllegalArgumentException(fileName + " not found!");
		}

		javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader();

		loader.setLocation(template);
		if (setController)
			loader.setController(controller);
		if (setRoot)
			loader.setRoot(controller);

		Platform.runLater(() -> {
			try {
				Node root = (Node) loader.load();
				if (controller instanceof LoaderInitializable) {
					Platform.runLater(() -> {
						((LoaderInitializable)controller).initialize(root);
						Platform.runLater(() -> onInitialized.accept(controller));
					});
				} else {
					Platform.runLater(() -> onInitialized.accept(controller));
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
			}
		});
	}

	/**
	 * Get File name based on the controller name
	 * MyPrettyClass then the resource is fxml/my-pretty-class.fxml
	 *
	 * @param controller object ot check the class
	 * @return URL to the resource
	 */
	private String getFileName(Object controller) {
		StringBuilder name = new StringBuilder();
		for (char c : controller.getClass().getSimpleName().toCharArray()) {
			if (Character.isUpperCase(c) && name.length() > 0) {
				name.append('-');
			}
			name.append(Character.toLowerCase(c));
		}
		return String.format(RESOURCE_PATH, name);
	}

	public static <T> void load(T val, Consumer<T> onLoad) {
		//noinspection unchecked
		new FXMLLoaderFactory(val, onLoad);
	}

	public static void load(LoaderCallback onLoad, Object... initializables) {
		loadCollection(onLoad, Arrays.asList(initializables));
	}

	public static void loadCollection(LoaderCallback onLoad, Collection<Object> initializables) {
		final IntegerProperty currentValue = new SimpleIntegerProperty(0);
		int totalAmmount = initializables.size();
		for (Object v : initializables) {
			if (v instanceof Loadable) {
				((Loadable)v).addListener(() -> {
					currentValue.set(currentValue.get() + 1);
					onLoad.loaded(totalAmmount, currentValue.get());
				});
			} else load(v, onload -> {
				currentValue.set(currentValue.get() + 1);
				onLoad.loaded(totalAmmount, currentValue.get());
			});
		}
	}
}
