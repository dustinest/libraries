package ee.fj.io.tablereader;

public interface TableReader {
	public boolean supports(SupportedFile type);
	// provide extension without comma
	public boolean supportsExtension(String extension);

	public boolean is(Class<? extends TableReader> reader);
	public <T extends TableReader> T as(Class<T> reader);
}
