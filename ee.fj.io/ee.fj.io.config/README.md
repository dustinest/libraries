# The input and output stream to read and write configuration.

The streams crafted to read and write primitives. In case your structure changes the stream reads and writes the version.

## Usage

See [Junit tests](src/test/java/ee/fj/io/config/ConfigFileTest.java) for basic use cases

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
			String lorem = (String)reader.read();
			String ipsum = (String)reader.read();
			int number = (Integer)reader.read();
			boolean trueFalse = (Boolean)reader.read();
			String myOtherLine = (String)reader.read();
		}
	}

Or 

	try (ConfigFileReader reader = new ConfigFileReader(Files.newInputStream(configuration))) {
		if (reader.getVersion() == 25) {
			read.read((type, value) -> {
				String lorem = type.as(String).getValue(value);
				return false;
			});
			String ipsum = reader.read(String.class);
			int number = reader.read(Integer.class);
			boolean trueFalse = reader.readValue(Boolean.class);
			String myOtherLine = reader.readValue(String.class);
		}
	}

The version is mandatory. Default version is set as 0

## Installation:

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
		<groupId>ee.fj.io</groupId>
		<version>0.0.2</version>
		<artifactId>ee.fj.io.config</artifactId>
	</dependency>
