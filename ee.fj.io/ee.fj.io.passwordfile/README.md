#Generate and read password protected files

## Usage

NB! There is a known problem with reading / writing larger files. You might consider using bytearray on reading and writing

Simple usage:

		try (OutputStream out = new PasswordProtectedOutputStream(outputstream, password)) {
			// write as your normal outputstream
		}

		try (InputStream in = new PasswordProtectedInputStream(inputstream, password) ) {
				//read as a normal inputstream
		}

The input/output streams uses default salt and most best encryption algorithm. It is possible to specify your own

Also see [SupportedAlgorithm.java](src/main/java/ee/fj/io/passwordfile/SupportedAlgorithm.java) for supported algorithm list. It eases up the usage. You can either use the best encoding from the list of select one on your own.

## Installation
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
		<version>0.0.1</version>
		<artifactId>ee.fj.io.passwordfile</artifactId>
	</dependency>
