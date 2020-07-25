package ee.fj.io.tablereader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ExcelReaderTest {
	private final String SASHLIK;
	private final String TYINA;

	public ExcelReaderTest() throws IOException {
		try (BufferedReader in =  new BufferedReader(new InputStreamReader(CsvReaderTest.class.getClassLoader().getResourceAsStream("test.txt"), StandardCharsets.UTF_8))) {
			this.SASHLIK = in.readLine();
			this.TYINA = in.readLine();
		}
	}

	@Test
	public void testSimpleXls() throws IOException {
		read(TableReaderFactory.getReaderByExtendsion(".xls").as(ExcelReader.class), "simple.xls");
	}

	@Test
	public void testSimpleXlsX() throws IOException {
		read(TableReaderFactory.getReaderByExtendsion(".xls").as(ExcelReader.class), "simple.xlsx");
	}


	private void read(ExcelReader reader, String fileName) throws IOException {
		int[] loadingAmount = {-1, -1, -1};
		try (InputStream in = ExcelReaderTest.class.getClassLoader().getResourceAsStream(fileName)) {
			reader.read(0, in, (type, row, col, value) -> {
				loadingAmount[0]++;
				if (row == 0 && col == 0) {
					// This is the problem with š in excel to csv.
					//Assertions.assertNotEquals("Šašikul on kole nälg", strings[0][0]); // �=65533,a=97,�=65533,i=105,k=107,u=117,l=108, =32,o=111,n=110, =32,k=107,o=111,l=108,e=101, =32,n=110,ä=228,l=108,g=103,
					Assertions.assertEquals(SASHLIK, TableResult.STRING.as(value));
					Assertions.assertSame(TableResult.STRING, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 1) {
					Assertions.assertEquals(1d, value);
					Assertions.assertSame(TableResult.DOUBLE, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 2) {
					LocalDateTime dt = TableResult.DATE.as(value);
					Assertions.assertEquals(21, dt.getDayOfMonth());
					Assertions.assertEquals(Month.JUNE, dt.getMonth());
					Assertions.assertEquals(2011, dt.getYear());
					Assertions.assertSame(TableResult.DATE, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 3) {
					LocalDateTime dt = TableResult.DATE.as(value);
					Assertions.assertEquals(21, dt.getDayOfMonth());
					Assertions.assertEquals(Month.JUNE, dt.getMonth());
					Assertions.assertEquals(2011, dt.getYear());
					Assertions.assertEquals(12, dt.getHour());
					Assertions.assertEquals(31, dt.getMinute());
					Assertions.assertEquals(41, dt.getSecond());
					Assertions.assertSame(TableResult.DATE, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 4) {
					Assertions.assertNull(value);
					Assertions.assertSame(TableResult.NULL, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 1 && col == 0) {
					Assertions.assertEquals(TYINA, value);
					Assertions.assertSame(TableResult.STRING, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 1 && col == 1) {
					Assertions.assertEquals(2.1123d, value);
					Assertions.assertSame(TableResult.DOUBLE, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 1 && col == 2) {
					Assertions.assertNull(value);
					Assertions.assertSame(TableResult.NULL, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 0) {
					Assertions.assertNull(value);
					Assertions.assertSame(TableResult.NULL, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 1) {
					Assertions.assertEquals(-1d, value);
					Assertions.assertSame(TableResult.DOUBLE, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 2) {
					Assertions.assertNull(value);
					Assertions.assertSame(TableResult.NULL, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 3) {
					Assertions.assertNull(value);
					Assertions.assertSame(TableResult.NULL, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 4) {
					Assertions.assertEquals("Kolmas", value);
					Assertions.assertSame(TableResult.STRING, type);
					Assertions.assertEquals(row, loadingAmount[1]);
				} else if (type == TableResult.BEGIN) {
					Assertions.assertEquals(-1, col);
					Assertions.assertEquals(-1, row);
					Assertions.assertNull(value);
				} else if (type == TableResult.END) {
					Assertions.assertEquals(-1, col);
					Assertions.assertEquals(-1, row);
					Assertions.assertNull(value);
				} else if (type == TableResult.ROW_START) {
					loadingAmount[1]++;
					Assertions.assertEquals(-1, col);
					Assertions.assertTrue(row > -1);
					Assertions.assertTrue(TableResult.ROW_START.as(value) > -1);
				} else if (type == TableResult.ROW_END) {
					loadingAmount[2]++;
					Assertions.assertEquals(loadingAmount[2], loadingAmount[1]);
					Assertions.assertEquals(-1, col);
					Assertions.assertTrue(row > -1);
					Assertions.assertTrue(TableResult.ROW_START.as(value) > -1);
				} else if (type == TableResult.NULL) {
					Assertions.assertTrue(col > 0, col + "> 0");
					Assertions.assertTrue(row > -1);
					Assertions.assertNull(value);
				} else {
					throw new IllegalArgumentException(row + "," + col + ": " + type + ": " + value);
				}
			});
		}
		Assertions.assertEquals(15, loadingAmount[0]);
		Assertions.assertEquals(2, loadingAmount[1]);
		Assertions.assertEquals(2, loadingAmount[2]);
	}

	@Test
	public void testTypes() {
		Assertions.assertNotNull(TableReaderFactory.getReader(SupportedFiles.EXCEL_2007).as(ExcelReader.class));
		Assertions.assertNotNull(TableReaderFactory.getReader(SupportedFiles.EXCEL_97).as(ExcelReader.class));
		for (String extension : new String[] {"xls", "xlsx"}) {
			Assertions.assertNotNull(TableReaderFactory.getReaderByExtendsion(extension).as(ExcelReader.class));
			Assertions.assertNotNull(TableReaderFactory.getReader("." + extension).as(ExcelReader.class));
			for (String name : new String[] {"mina", "see.mina", ""}) {
				Assertions.assertNotNull(TableReaderFactory.getReader(name + "." + extension).as(ExcelReader.class));
			}
		}
	}

}

