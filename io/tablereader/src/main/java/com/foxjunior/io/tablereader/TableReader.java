package com.foxjunior.io.tablereader;

public interface TableReader {
	/**
	 * Test if reader supports that file type
	 * @return true if table rader supports this particular supported type
	 */
	boolean supports(SupportedFiles type);

	/**
	 * Test the file extension if is supported.
	 * @return true if extension is supported.
	 */
	boolean supportsExtension(String extension);

	/**
	 * Test if Table reader is the instance expected
	 */
	boolean is(Class<? extends TableReader> reader);

	/**
	 * Simple casting helper method
	 */
	<T extends TableReader> T as(Class<T> reader);
}
