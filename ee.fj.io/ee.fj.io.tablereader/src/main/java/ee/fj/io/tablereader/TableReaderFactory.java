package ee.fj.io.tablereader;

import java.io.File;

public class TableReaderFactory {
	private static final TableReader[] readers = new TableReader[] {
			new ExcelReader(),
			new CsvReader()
	};
	public static TableReader getReader(SupportedFiles type) {
		for (TableReader s : readers) {
			if (s.supports(type))
				return s;
		}
		throw new IllegalArgumentException(type + " is not supported!");
	}

	public static TableReader getReader(File fileName) {
		return getReader(fileName.getName());
	}

	public static TableReader getReader(String fileName) {
		int i = fileName.lastIndexOf('.');
		if (i < 0) {
			throw new IllegalArgumentException("Suffix of the name " + fileName + " does not exist!");
		}
		try {
			return getReaderByExtendsion(fileName.substring(i+1));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Suffix of the name " + fileName + " is not supported!", e);
		}
	}

	public static TableReader getReaderByExtendsion(String suffix) {
		int i = suffix.lastIndexOf('.');
		String _suffix = suffix;
		if (i >= 0) {
			_suffix = _suffix.substring(i+1);
		}
		_suffix = _suffix.toLowerCase();
		for (TableReader s : readers) {
			if (s.supportsExtension(_suffix)) {
				return s;
			}
		}
		throw new IllegalArgumentException(suffix + " is not supported!");
	}
}
