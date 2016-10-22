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
		String fileName = null;
		if (controller.getClass().isAnnotationPresent(FXMLLoader.class)) {
			FXMLLoader annotation = (FXMLLoader)controller.getClass().getAnnotation(FXMLLoader.class);
			_controller = annotation.controller();
			_root = annotation.root();
			fileName = annotation.fileName();
		}

		if (fileName == null)
			fileName = getFileName(controller);

		URL template = controller.getClass().getResource(fileName);
		if (template == null) {
			throw new IllegalArgumentException(fileName + " not found!");
		}
		
		javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader();

		loader.setLocation(template);
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
	 * Get File name based on the controller name
	 * MyPrettyClass then the resource is fxml/my-pretty-class.fxml
	 * 
	 * @param container
	 * @return URL to the resource
	 * @throws IOException
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void load(T val, Consumer<T> onLoad) {
		new FXMLLoaderFactory(val, onLoad);
	}
}
