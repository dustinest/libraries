# The input and output stream to read and write configuration.

The streams crafted to read and write primitives. In case your structure changes the stream reads and writes the version.

## Usage

See [Junit tests](src/test/java/com/foxjunior/io/config/ConfigFileTest.java) for basic use cases

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
		if (reader.getVersion() == 25) {
			String lorem = (String)reader.readNext();
			String ipsum = (String)reader.readNext();
			int number = (Integer)reader.readNext();
			boolean trueFalse = (Boolean)reader.readNext();
			String myOtherLine = (String)reader.readNext();
		}
	}

Or 

	try (ConfigFileReader reader = new ConfigFileReader(Files.newInputStream(configuration))) {
		if (reader.getVersion() == 25) {
			read.read((type, value) -> {
				String lorem = type.as(String).getValue(value);
				return false;
			});
			String ipsum = reader.readNext(String.class);
			int number = reader.readNext(Integer.class);
			boolean trueFalse = reader.readNext(Boolean.class);
			String myOtherLine = reader.readNext(String.class);
		}
	}

The version is mandatory. Default version is set as 0
