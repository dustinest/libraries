package ee.fj.io.tablereader;

public enum SupportedFile {
	CSV("Comma separated list (csv)", "csv"),
	TXT("Comma separated list (txt)", "txt"),
	EXCEL_97("Microsoft Excel 97/2000/XP/2003 (xls)", "xls"),
	EXCEL_2007("Microsoft Excel 2007&2010/2013 XML (xlsx)", "xlsx");

	public final String label;
	public final String extension;
	public final String filter;
	private SupportedFile(String label, String extension) {
		this.label = label;
		this.extension = extension;
		this.filter = "*." + extension;
	}
}
