# Helper libraries to read excel and CSV files

Code snippet:

			TableReaderFactory.getReader(SupportedFile.CSV).as(CsvReader.class).read(
				getInputStream("simple.csv", Charset.forName("Windows-1257")),
				',', '"',
				new TableReaderListener() {
				// callbacks
				}

Or for excel

		TableReaderFactory.getReader("simple.xlsx").as(ExcelReader.class).read(0, getInputStream("simple.xlsx"), new TableReaderListener() {
			// callback
		}
				
	
See test cases for more help

TODO: clean up API to make it more easy to use and support lambdas