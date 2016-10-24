# FXML loaders

I found myself reading JavaFx .FXML files all over and over again. So, I created a simple wrapper to help me with that.

# Installation

Add repository to your ``pom.xml``:

	<repositories>
		<repository>
			<id>ee.fj-mvn-repo</id>
			<url>https://raw.githubusercontent.com/dustinest/libraries/mvn-repo</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>

And dependency:

	<dependency>
		<groupId>ee.fj.javafx</groupId>
		<artifactId>ee.fj.javafx.loader</artifactId>
		<version>0.0.1</version>
	</dependency>

# Usage:

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

In case you already set controller in your fxml you can add additional argument to your FXMLLoader annotation: ``@FXMLLoader(controller=false)``

The annotation is not required. The only purpose for now is to limit the loader functionality.

If you want to show the loading status and have more control over your loading cycle, call:

	FXMLLoaderFactory.load((total, status) -> {
		if (total == status) {
			// launch your application
		}
		System.out.println("initialized " + status + " out of " total);
	}, controller1, controller2, etc...);

Sometimes you want to use FXMLLoaderFactory for the classes or objects which do not need any initialization. Or you use the controller in FXML. For that you can use Loadable interface. For example:

	class MyOtherModule implements Loadable {
			private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);

			public MyOtherModule() {
				super();
				FXMLLoaderFactory.load(this, theInstance -> iasLoaded.set(true) );
			}
		
			public MyOtherModule(Runnable onLoad) {
				this();
				addListener(onLoad);
			}

			@Override
			public void addListener(Runnable isLoaded) {
				this.isLoaded.addListener((ob, oldValue, newValue) -> {
					if (newValue) {
						isLoaded.run();
					}
				});;
				if (this.isLoaded.get()) {
					isLoaded.run();
				}
			}
	}

Now you can use FXMLLoaderFactory for your multiple classes and track loading

	FXMLLoaderFactory.load((total, status) -> {
		if (total == status) {
			// launch your application
		}
		System.out.println("initialized " + status + " out of " total);
	}, controller1, controller2, new MyOtherModule());

The behaviour of Loadable interface might get smarter in the future to make its use easier.


