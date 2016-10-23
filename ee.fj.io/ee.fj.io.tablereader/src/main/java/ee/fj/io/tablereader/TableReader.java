package ee.fj.io.tablereader;

public interface TableReader {
	/**
	 * Test if reader supports that file type 
	 * @param type
	 * @return true if table rader supports this particular supported type
	 */
	public boolean supports(SupportedFiles type);

	/**
	 * Test the file extension if is supported.
	 * @param extension
	 * @return true if extension is supported.
	 */
	public boolean supportsExtension(String extension);

	/**
	 * Test if Table reader is the instance expected
	 * @param reader
	 * @return
	 */
	public boolean is(Class<? extends TableReader> reader);

	/**
	 * Simple casting helper method
	 * @param reader
	 * @return
	 */
	public <T extends TableReader> T as(Class<T> reader);
}
