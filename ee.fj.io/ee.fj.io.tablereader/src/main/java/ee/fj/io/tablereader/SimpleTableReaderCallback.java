package ee.fj.io.tablereader;

import java.util.Date;

public class SimpleTableReaderCallback implements TableReaderCallback {
	@Override public void read(int row, int col, String value) { }

	@Override
	public void read(int row, int col, Date value) {
	}

	@Override
	public void read(int row, int col, Double value) {
	}

	@Override
	public void read(int row, int col, Boolean value) {
	}

	@Override
	public void read(int row, int col) {
	}

	@Override
	public void rowStart(int row) {
	}

	@Override
	public void rowEnd(int row) {
	}

	@Override
	public void fileStarted(int sheet, String sheetName) {
	}

	@Override
	public void fileFinished(int sheet, String sheetName) {
	}

}
