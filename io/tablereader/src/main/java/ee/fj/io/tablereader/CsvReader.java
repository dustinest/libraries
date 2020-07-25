package ee.fj.io.tablereader;

import java.io.IOException;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

public class CsvReader implements TableReader {

	public void read(Reader reader, TableResult callback) throws IOException {
		read(new CSVReader(reader), callback);
	}

	public void read(Reader reader, char separator, char quatationChar, TableResult callback) throws IOException {
		read(new CSVReader(reader, separator, quatationChar, false), callback);
	}

	public void read(Reader reader, char separator, TableResult callback) throws IOException {
		read(new CSVReader(reader, separator), callback);
	}

	public void read(CSVReader reader, TableResult callback) throws IOException {
		callback.read(TableResult.BEGIN, -1, -1, null);
		try {
			int row = 0;
			for (String[] value = reader.readNext(); value != null; value = reader.readNext()) {
				callback.read(TableResult.ROW_START, row, -1, row);
				for (int i = 0; i < value.length; i++) {
					if (value[i] == null || value[i].length() == 0) {
						callback.read(TableResult.NULL, row, i, null);
					} else {
						callback.read(TableResult.STRING, row, i, value[i]);
					}
				}
				callback.read(TableResult.ROW_END, row, -1, row);
				row ++;
			}
		} finally {
			callback.read(TableResult.END, -1, -1, null);
		}
	}

	@Override
	public boolean supports(SupportedFiles type) {
		return (type == SupportedFiles.CSV || type == SupportedFiles.TXT);
	}

	@Override
	public boolean supportsExtension(String extension) {
		return SupportedFiles.CSV.extension.equals(extension) || SupportedFiles.TXT.extension.equals(extension);
	}

	@Override
	public boolean is(Class<? extends TableReader> reader) {
		return reader.isAssignableFrom(CsvReader.class);
	}

	@Override
	public <T extends TableReader> T as(Class<T> reader) {
		if (is(reader)) {
			//noinspection unchecked
			return (T) this;
		}
		return null;
	}
}
