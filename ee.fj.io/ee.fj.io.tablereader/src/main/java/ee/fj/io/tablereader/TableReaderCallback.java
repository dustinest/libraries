package ee.fj.io.tablereader;

import java.util.Date;

public interface TableReaderCallback {
	public void read(int row, int col, String value);
	public void read(int row, int col, Date value);
	public void read(int row, int col, Double value);
	public void read(int row, int col, Boolean value);
	/**
	 * Read empty value
	 * @param row
	 * @param col
	 */
	public void read(int row, int col);

	public void rowStart(int row);
	public void rowEnd(int row);
	public void fileStarted(int sheet, String sheetName);
	public void fileFinished(int sheet, String sheetName);
}
