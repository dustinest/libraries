package com.foxjunior.io.tablereader;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
	public void read(int sheet, InputStream in, TableResult reader) throws IOException {
		try (Workbook wb = WorkbookFactory.create(in)) {
			Sheet _sheet = wb.getSheetAt(sheet);
			String sheetName = _sheet.getSheetName();
			reader.read(TableResult.BEGIN, -1, -1, null);
			for (Row row : _sheet) {
				reader.read(TableResult.ROW_START, row.getRowNum(), -1, row.getRowNum());
				for (Cell c : row) {
					CellType cellType = c.getCellTypeEnum();
					if (cellType == CellType.BLANK) {
						reader.read(TableResult.NULL, row.getRowNum(), c.getColumnIndex(), null);
					} else if (cellType == CellType.BOOLEAN) {
						reader.read(TableResult.BOOLEAN, row.getRowNum(), c.getColumnIndex(), c.getBooleanCellValue());
					} else if (cellType == CellType.ERROR) {
						//reader.read(row.getRowNum(), c.getColumnIndex(), c.getErrorCellValue());
					} else if (cellType == CellType.FORMULA) {
						reader.read(TableResult.STRING, row.getRowNum(), c.getColumnIndex(), c.getCellFormula());
					} else if (cellType == CellType.NUMERIC) {
						if (HSSFDateUtil.isCellDateFormatted(c)) {
							reader.read(TableResult.DATE, row.getRowNum(), c.getColumnIndex(), LocalDateTime.ofInstant(c.getDateCellValue().toInstant(), ZoneId.systemDefault()));
						} else {
							reader.read(TableResult.DOUBLE, row.getRowNum(), c.getColumnIndex(), c.getNumericCellValue());
						}
					} else if (cellType == CellType.STRING) {
						reader.read(TableResult.STRING, row.getRowNum(), c.getColumnIndex(), c.getStringCellValue());
					} else {
						throw new IOException("Unknown type: " + cellType + " is not supported!");
					}
				}
				reader.read(TableResult.ROW_END, row.getRowNum(), -1, row.getRowNum());
			}
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			throw new IOException(e);
		} finally {
			reader.read(TableResult.END, -1, -1, null);
		}
	}

	@Override
	public boolean supports(SupportedFiles type) {
		return SupportedFiles.EXCEL_2007 == type || SupportedFiles.EXCEL_97 == type;
	}

	@Override
	public boolean supportsExtension(String extension) {
		return SupportedFiles.EXCEL_2007.extension.equals(extension) || SupportedFiles.EXCEL_97.extension.equals(extension);
	}

	@Override
	public boolean is(Class<? extends TableReader> reader) {
		return reader.isAssignableFrom(ExcelReader.class);
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
