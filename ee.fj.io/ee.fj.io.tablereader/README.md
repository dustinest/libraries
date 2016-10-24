# Helper libraries to read excel and CSV files

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
		<artifactId>ee.fj.io.tablereader</artifactId>
		<version>0.0.1</version>
	</dependency>


## Code snippet:

			TableReaderFactory.getReader(SupportedFiles.CSV).as(CsvReader.class).read(
				new InputStreamReader(inputStream, Charset.forName("Windows-1257")),
				',', '"', (type, row, column, value) -> {
		
			// callback
		});

Or for excel

		TableReaderFactory.getReader("simple.xlsx").as(ExcelReader.class).read(0, inputStream, (type, row, column, value) -> {
		
			// callback
		});

There type is:

- TableResult.BEGIN
- TableResult.END
- TableResult.ROW_START
- TableResult.ROW_END
- TableResult.STRING
- TableResult.LONG
- TableResult.DOUBLE
- TableResult.DATE
- TableResult.BOOLEAN
- TableResult.NULL

To easy cast the value use:

type.as(value), for instance:

	if (type == TableResult.STRING) {
		LocalDateTime realValue = TableResult.DATE.as(value);
	}

If you are looking for file Type supported, check ``SupportedFiles`` enumeratuion. Also see ``TableReaderFactory`` for more entry point references.

See test cases for more help
