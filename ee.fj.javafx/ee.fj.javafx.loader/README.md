# FXML loaders

I found myself reading JavaFx .FXML files all over and over again. So, I created a simple wrapper to help me with that.

Simple usage:

My controller class, which extends Vbox:
	
	MyController controller = new MyController();

Fxml file must be stored under fxml directory and named as my-controller.fxml (fxml/my-controller-fxml)

	FXMLLoaderFactory.load(controller, controller -> {
		// Controller is read, ready to load and manipulated
	});

If you, for instance have dialog which has no root then use annotation:

	@FXMLLoader(root=false)
	MyController extends Dialog implements LoaderInitializable {
		@Override
		public void initialize(Node node) {
			getDialogPane().setContent(node);
		}
	}

If you want to show the loading status and have more control over your loading cycle, call:

	CompoundLoader.load((total, status) -> {
		if (total == status) {
			// launch your application
		}
		System.out.println("initialized " + status + " out of " total);
	}, controller1, controller2, etc...);