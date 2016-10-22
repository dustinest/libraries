package ee.fj.javafx.loader;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.Node;

public class FXMLLoaderFactory<T> {
	private final String RESOURCE_PATH = "/fxml/%s.fxml";
	private static final Logger LOGGER = Logger.getLogger(FXMLLoaderFactory.class.getName());

	protected FXMLLoaderFactory(T controller, Consumer<T> onInitialized) {
		boolean _controller = true;
		boolean _root = true;
		if (controller.getClass().isAnnotationPresent(FXMLLoader.class)) {
			FXMLLoader annotation = (FXMLLoader)controller.getClass().getAnnotation(FXMLLoader.class);
			_controller = annotation.controller();
			_root = annotation.root();
			
		}
		javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader();

		loader.setLocation(getViewURL(controller));
		if (_controller)
			loader.setController(controller);
		if (_root)
			loader.setRoot(controller);

		Platform.runLater(() -> {
			try {
				Node root = (Node) loader.load();
				if (controller instanceof LoaderInitializable) {
					Platform.runLater(() -> {
						((LoaderInitializable)controller).initialize(root);
						Platform.runLater(() -> {
							onInitialized.accept(controller);
						});
					});
				} else {
					Platform.runLater(() -> {
						onInitialized.accept(controller);
					});
				}
			} catch (IOException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
			}
		});
	}

	/**
	 * Get resource name based on the class name. If the class name is
	 * MyPrettyClass then the resource is fxml/my-pretty-class.fxml
	 * 
	 * @param container
	 * @return URL to the resource
	 * @throws IOException
	 */
	private URL getViewURL(Object container) {
		StringBuilder name = new StringBuilder();
		for (char c : container.getClass().getSimpleName().toCharArray()) {
			if (Character.isUpperCase(c) && name.length() > 0) {
				name.append('-');
			}
			name.append(Character.toLowerCase(c));
		}
		String path = String.format(RESOURCE_PATH, name);
		URL rv = container.getClass().getResource(path);
		if (rv == null) {
			throw new IllegalArgumentException(path + " not found!");
		}
		return rv;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void load(T val, Consumer<T> onLoad) {
		new FXMLLoaderFactory(val, onLoad);
	}
}
