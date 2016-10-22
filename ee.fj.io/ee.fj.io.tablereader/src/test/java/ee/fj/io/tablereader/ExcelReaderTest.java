package ee.fj.io.tablereader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;


public class ExcelReaderTest {
	private InputStream getInputStream(String fileName) {
		return ExcelReaderTest.class.getClassLoader().getResourceAsStream(fileName);
	}

	@Test
	public void testXlsX() throws IOException {
		final boolean[] readEmpty = new boolean[]{false};
		final boolean[] readBoolean = new boolean[]{false};
		final double[] numbers = new double[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
		final String[] strings = new String[] {null, null, null};
		final boolean[] fileWorked = new boolean[] {false, false};
		final Date[] dates = new Date[] {null, null};
		
		TableReaderFactory.getReader("simple.xlsx").as(ExcelReader.class).read(0, getInputStream("simple.xlsx"), new TableReaderCallback() {
			
			@Override
			public void read(int row, int col) {
				readEmpty[row] = true;
			}
			
			@Override
			public void read(int row, int col, Boolean value) {
				readBoolean[row] = true;
			}
			
			@Override
			public void read(int row, int col, Double value) {
				numbers[row] = value;
			}
			
			@Override
			public void read(int row, int col, Date value) {
				dates[col-2] = value;
			}
			
			@Override
			public void read(int row, int col, String value) {
				strings[row] = value;
			}
			
			@Override public void fileStarted(int sheet, String sheetName) {
				fileWorked[0] = true;
				Assert.assertEquals(0, sheet);
				Assert.assertEquals("simple", sheetName);
			}

			@Override public void fileFinished(int sheet, String sheetName) {fileWorked[1] = true;  }
			@Override public void rowStart(int row) {}
			@Override public void rowEnd(int row) { }
		});
		Assert.assertEquals(true, fileWorked[1]);
		Assert.assertEquals(true, fileWorked[1]);

		Assert.assertEquals(false, readEmpty[0]);
		Assert.assertEquals(false, readBoolean[0]);
		Assert.assertEquals(1.0d, numbers[0], 0);
		Assert.assertEquals(2.1123d, numbers[1], 0);
		Assert.assertEquals(-1.0d, numbers[2], 0);
		Assert.assertEquals("Šašikul on kole nälg", strings[0]);
		Assert.assertEquals("Tüina", strings[1]);
		Assert.assertEquals("Kolmas", strings[2]);
		Calendar cal = Calendar.getInstance();
		cal.set(2011, Calendar.JUNE, 21, 00, 00, 00);
		Assert.assertTrue("Dates aren't close enough to each other!", (cal.getTime().getTime() - dates[0].getTime()) < 1000);
		cal.set(2011, Calendar.JUNE, 21, 12, 31, 41);
		Assert.assertTrue("Dates aren't close enough to each other!", (cal.getTime().getTime() - dates[1].getTime()) < 1000);
	}

	@Test
	public void testXls() throws IOException {
		final boolean[] readEmpty = new boolean[]{false};
		final boolean[] readBoolean = new boolean[]{false};
		final double[] numbers = new double[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
		final String[] strings = new String[] {null, null, null};
		final boolean[] fileWorked = new boolean[] {false, false};
		final Date[] dates = new Date[] {null, null};
		TableReaderFactory.getReaderByExtendsion(".xls").as(ExcelReader.class).read(0, getInputStream("simple.xls"), new TableReaderCallback() {
			
			@Override
			public void read(int row, int col) {
				readEmpty[row] = true;
			}
			
			@Override
			public void read(int row, int col, Boolean value) {
				readBoolean[row] = true;
			}
			
			@Override
			public void read(int row, int col, Double value) {
				numbers[row] = value;
			}
			
			@Override
			public void read(int row, int col, Date value) {
				dates[col-2] = value;
			}
			
			@Override
			public void read(int row, int col, String value) {
				strings[row] = value;
			}

			@Override public void fileStarted(int sheet, String sheetName) {
				fileWorked[0] = true;
				Assert.assertEquals(0, sheet);
				Assert.assertEquals("simple", sheetName);
			}

			@Override public void fileFinished(int sheet, String sheetName) {fileWorked[1] = true;  }
			@Override public void rowStart(int row) {}
			@Override public void rowEnd(int row) { }
		});
		Assert.assertEquals(true, fileWorked[1]);
		Assert.assertEquals(true, fileWorked[1]);

		Assert.assertEquals(false, readEmpty[0]);
		Assert.assertEquals(false, readBoolean[0]);
		Assert.assertEquals(1.0d, numbers[0], 0);
		Assert.assertEquals(2.1123d, numbers[1], 0);
		Assert.assertEquals(-1.0d, numbers[2], 0);
		Assert.assertEquals("Šašikul on kole nälg", strings[0]);
		Assert.assertEquals("Tüina", strings[1]);
		Assert.assertEquals("Kolmas", strings[2]);
		Calendar cal = Calendar.getInstance();
		cal.set(2011, Calendar.JUNE, 21, 00, 00, 00);
		Assert.assertTrue("Dates aren't close enough to each other!", (cal.getTime().getTime() - dates[0].getTime()) < 1000);
		cal.set(2011, Calendar.JUNE, 21, 12, 31, 41);
		Assert.assertTrue("Dates aren't close enough to each other!", (cal.getTime().getTime() - dates[1].getTime()) < 1000);
	}

	@Test
	public void testTypes() {
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFile.EXCEL_2007).as(ExcelReader.class));
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFile.EXCEL_97).as(ExcelReader.class));
		for (String extension : new String[] {"xls", "xlsx"}) {
			Assert.assertNotNull(TableReaderFactory.getReaderByExtendsion(extension).as(ExcelReader.class));
			Assert.assertNotNull(TableReaderFactory.getReader("." + extension).as(ExcelReader.class));
			for (String name : new String[] {"mina", "see.mina", ""}) {
				Assert.assertNotNull(TableReaderFactory.getReader(name + "." + extension).as(ExcelReader.class));
			}
		}
	}

}

