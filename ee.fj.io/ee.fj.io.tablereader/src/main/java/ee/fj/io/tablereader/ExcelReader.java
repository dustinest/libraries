package ee.fj.io.tablereader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class ExcelReader implements TableReader {

	public void read(int sheet, InputStream in, TableReaderCallback reader) throws IOException {
		String sheetName = null;
		try (Workbook wb = WorkbookFactory.create(in)) {
			Sheet _sheet = wb.getSheetAt(sheet);
			sheetName = _sheet.getSheetName();
			reader.fileStarted(sheet, sheetName);
			for (Row row : _sheet) {
				reader.rowStart(row.getRowNum());
				for (Cell c : row) {
					CellType cellType = c.getCellTypeEnum();
					if (cellType == CellType.BLANK) {
						reader.read(row.getRowNum(), c.getColumnIndex());
					} else if (cellType == CellType.BOOLEAN) {
						reader.read(row.getRowNum(), c.getColumnIndex(), c.getBooleanCellValue());
					} else if (cellType == CellType.ERROR) {
						//reader.read(row.getRowNum(), c.getColumnIndex(), c.getErrorCellValue());
					} else if (cellType == CellType.FORMULA) {
						reader.read(row.getRowNum(), c.getColumnIndex(), c.getCellFormula());
					} else if (cellType == CellType.NUMERIC) {
						if (HSSFDateUtil.isCellDateFormatted(c)) {
							reader.read(row.getRowNum(), c.getColumnIndex(), c.getDateCellValue());
						} else {
							reader.read(row.getRowNum(), c.getColumnIndex(), c.getNumericCellValue());
						}
					} else if (cellType == CellType.STRING) {
						reader.read(row.getRowNum(), c.getColumnIndex(), c.getStringCellValue());
					} else {
						throw new IOException("Unknown type: " + cellType + " is not supported!");
					}
				}
				reader.rowEnd(row.getRowNum());
			}
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			throw new IOException(e);
		} finally {
			reader.fileFinished(sheet, sheetName);
		}
	}

	@Override
	public boolean supports(SupportedFile type) {
		return SupportedFile.EXCEL_2007 == type || SupportedFile.EXCEL_97 == type;
	}

	@Override
	public boolean supportsExtension(String extension) {
		return SupportedFile.EXCEL_2007.extension.equals(extension) || SupportedFile.EXCEL_97.extension.equals(extension);
	}

	@Override
	public boolean is(Class<? extends TableReader> reader) {
		return reader.isAssignableFrom(ExcelReader.class);
	}

	@Override
	public <T extends TableReader> T as(Class<T> reader) {
		if (is(reader)) {
			return (T) this;
		}
		return null;
	}

}
