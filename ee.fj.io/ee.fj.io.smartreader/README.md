# Automatically detect text file encodings.

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
		<artifactId>ee.fj.io.smartreader</artifactId>
		<version>0.0.1</version>
	</dependency>

## Usage
Use CharsetAwareInputStream for an entry point:

	CharsetAwareInputStream in = CharsetAwareInputStream.predict(InputSream in , Charset failbackCharset;

or

	CharsetAwareInputStream in = CharsetAwareInputStream.predict(InputStream in)

To get the charset:

		Charset charset_used = in.getCharset():
	
Recognizes UTF_8, UTF8 Little Endian and Big Endian, fails back to system default or preferred charset.

TODO: add more charsets to regognize