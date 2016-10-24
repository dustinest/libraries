# Predict the column type based on the matchers

Easy way to detect column types if they are e-mails or urls or something else...

[See test cases for more info](src/test/java/ee/fj/utils/columnpredictor/ColumnPredictorTest.java)

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
		<artifactId>ee.fj.utils.columnpredictor</artifactId>
		<version>0.0.1</version>
	</dependency>

