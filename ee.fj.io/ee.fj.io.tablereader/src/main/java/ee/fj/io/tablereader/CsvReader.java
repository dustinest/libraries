package ee.fj.io.tablereader;

import java.io.IOException;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

public class CsvReader implements TableReader {
	public void read(Reader reader, TableReaderCallback listener) throws IOException {
		read(new CSVReader(reader), listener);
	}

	public void read(Reader reader, char separator, char quatationChar, TableReaderCallback listener) throws IOException {
		read(new CSVReader(reader, separator, quatationChar, false), listener);
	}

	public void read(Reader reader, char separator, TableReaderCallback listener) throws IOException {
		read(new CSVReader(reader, separator), listener);
	}

	public void read(CSVReader reader, TableReaderCallback listener) throws IOException {
		listener.fileStarted(0, null);
		try {
			int row = 0;
			for (String[] value = reader.readNext(); value != null; value = reader.readNext()) {
				listener.rowStart(row);
				for (int i = 0; i < value.length; i++) {
					if (value[i] == null || value[i].length() == 0) {
						listener.read(row, i);
					} else {
						listener.read(row, i, value[i]);
					}
				}
				listener.rowEnd(row);
				row ++;
			}
		} finally {
			listener.fileFinished(0, null);
		}
	}

	@Override
	public boolean supports(SupportedFile type) {
		return (type == SupportedFile.CSV || type == SupportedFile.TXT);
	}

	@Override
	public boolean supportsExtension(String extension) {
		return SupportedFile.CSV.extension.equals(extension) || SupportedFile.TXT.extension.equals(extension);
	}

	@Override
	public boolean is(Class<? extends TableReader> reader) {
		return reader.isAssignableFrom(CsvReader.class);
	}

	@Override
	public <T extends TableReader> T as(Class<T> reader) {
		if (is(reader)) {
			return (T) this;
		}
		return null;
	}
}
