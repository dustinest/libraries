# Image utils.

The purpose of this library is to scale and resize images

## Usage

See [ImageResizer.java](src/main/java/ee/fj/awt/image/ImageResizer.java) for basic use cases

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
		<groupId>ee.fj.awt</groupId>
		<version>0.0.1</version>
		<artifactId>ee.fj.awt.image</artifactId>
	</dependency>
