package ee.fj.io.tablereader;

public enum SupportedFiles {
	CSV("Comma separated list", "csv"),
	TXT("Comma separated list", "txt"),
	EXCEL_97("Microsoft Excel 97/2000/XP/2003", "xls"),
	EXCEL_2007("Microsoft Excel 2007&2010/2013 XML", "xlsx");

	public final String label;
	public final String extension;
	public final String filter;
	private SupportedFiles(String label, String extension) {
		this.label = label;
		this.extension = extension;
		this.filter = "*." + extension;
	}
}
