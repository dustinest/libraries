# The input and output stream to read and write configuration.

The streams crafted to read and write primitives. In case your structure changes the stream reads and writes the version.

## Simple use case


Define your version (mandatory, default is 0)

	int version = 25;


Define a path

	Path configuration = Paths.get("configuration.dat");

Write the file lines


	try (ConfigFileWriter writer = new ConfigFileWriter(version, Files.newOutputStream(configuration))) {
		writer.write("Lorem", "Ipsum", 1, true);
		writer.write("My other line");
	}



Read the file


	try (ConfigFileReader reader = new ConfigFileReader(Files.newInputStream(configuration))) {
		Object[] lines = reader.read();
		if (reader.getVersion() == 25) {
	
			String lorem = (String)lines[0];
			String ipsum = (String)lines[2];
			int number = (Integer)lines[3];
			boolean trueFalse = (Boolean)lines[4];
			lines = reader.read();
	
			String myOtherLine = lines[0];
		}
	}

The version is mandatory. Default version is set as 0