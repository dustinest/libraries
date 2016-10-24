# Helper library to get mime types when Files.probeContentType is not enough.

	FileTypes.probeContentType(Path path)

throws IllegalArgumentException when mime type is not found

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
		<groupId>ee.fj.utils</groupId>
		<artifactId>ee.fj.utils.filetypes</artifactId>
		<version>0.0.1</version>
	</dependency>
