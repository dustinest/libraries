package ee.fj.io.tablereader;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;


public class ExcelReaderTest {
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
					//Assert.assertNotEquals("Šašikul on kole nälg", strings[0][0]); // �=65533,a=97,�=65533,i=105,k=107,u=117,l=108, =32,o=111,n=110, =32,k=107,o=111,l=108,e=101, =32,n=110,ä=228,l=108,g=103,
					Assert.assertTrue("Šašikul on kole nälg", TableResult.STRING.as(value).endsWith("ikul on kole nälg"));
					Assert.assertTrue(type == TableResult.STRING);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 1) {
					Assert.assertEquals(1d, value);
					Assert.assertTrue(type == TableResult.DOUBLE);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 2) {
					LocalDateTime dt = TableResult.DATE.as(value);
					Assert.assertEquals(21, dt.getDayOfMonth());
					Assert.assertEquals(Month.JUNE, dt.getMonth());
					Assert.assertEquals(2011, dt.getYear());
					Assert.assertTrue(type == TableResult.DATE);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 3) {
					LocalDateTime dt = TableResult.DATE.as(value);
					Assert.assertEquals(21, dt.getDayOfMonth());
					Assert.assertEquals(Month.JUNE, dt.getMonth());
					Assert.assertEquals(2011, dt.getYear());
					Assert.assertEquals(12, dt.getHour());
					Assert.assertEquals(31, dt.getMinute());
					Assert.assertEquals(41, dt.getSecond());
					Assert.assertTrue(type == TableResult.DATE);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 0 && col == 4) {
					Assert.assertNull(value);
					Assert.assertTrue(type == TableResult.NULL);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 1 && col == 0) {
					Assert.assertEquals("Tüina", value);
					Assert.assertTrue(type == TableResult.STRING);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 1 && col == 1) {
					Assert.assertEquals(2.1123d, value);
					Assert.assertTrue(type == TableResult.DOUBLE);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 1 && col == 2) {
					Assert.assertNull(value);
					Assert.assertTrue(type == TableResult.NULL);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 0) {
					Assert.assertNull(value);
					Assert.assertTrue(type == TableResult.NULL);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 1) {
					Assert.assertEquals(-1d, value);
					Assert.assertTrue(type == TableResult.DOUBLE);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 2) {
					Assert.assertNull(value);
					Assert.assertTrue(type == TableResult.NULL);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 3) {
					Assert.assertNull(value);
					Assert.assertTrue(type == TableResult.NULL);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (row == 2 && col == 4) {
					Assert.assertEquals("Kolmas", value);
					Assert.assertTrue(type == TableResult.STRING);
					Assert.assertEquals(row, loadingAmount[1]);
				} else if (type == TableResult.BEGIN) {
					Assert.assertEquals(-1, col);
					Assert.assertEquals(-1, row);
					Assert.assertNull(value);
				} else if (type == TableResult.END) {
					Assert.assertEquals(-1, col);
					Assert.assertEquals(-1, row);
					Assert.assertNull(value);
				} else if (type == TableResult.ROW_START) {
					loadingAmount[1]++;
					Assert.assertEquals(-1, col);
					Assert.assertTrue(row > -1);
					Assert.assertTrue(TableResult.ROW_START.as(value) > -1);
				} else if (type == TableResult.ROW_END) {
					loadingAmount[2]++;
					Assert.assertEquals(loadingAmount[2], loadingAmount[1]);
					Assert.assertEquals(-1, col);
					Assert.assertTrue(row > -1);
					Assert.assertTrue(TableResult.ROW_START.as(value) > -1);
				} else if (type == TableResult.NULL) {
					Assert.assertTrue(col + "> 0", col > 0);
					Assert.assertTrue(row > -1);
					Assert.assertNull(value);
				} else {
					throw new IllegalArgumentException(row + "," + col + ": " + type + ": " + value);
				}
			});
		}
		Assert.assertEquals(15, loadingAmount[0]);
		Assert.assertEquals(2, loadingAmount[1]);
		Assert.assertEquals(2, loadingAmount[2]);
	}
	
	@Test
	public void testTypes() {
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFiles.EXCEL_2007).as(ExcelReader.class));
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFiles.EXCEL_97).as(ExcelReader.class));
		for (String extension : new String[] {"xls", "xlsx"}) {
			Assert.assertNotNull(TableReaderFactory.getReaderByExtendsion(extension).as(ExcelReader.class));
			Assert.assertNotNull(TableReaderFactory.getReader("." + extension).as(ExcelReader.class));
			for (String name : new String[] {"mina", "see.mina", ""}) {
				Assert.assertNotNull(TableReaderFactory.getReader(name + "." + extension).as(ExcelReader.class));
			}
		}
	}

}

