package ee.fj.io.tablereader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class CsvReaderTest {
	private Reader getInputStream(String fileName, Charset encoding) {
		return new InputStreamReader(CsvReaderTest.class.getClassLoader().getResourceAsStream(fileName), encoding);
	}

	@Test
	public void simpleTest() throws IOException {
		final boolean[] readBoolean = new boolean[]{false};
		final boolean[] fileWorked = new boolean[] {false, false};
		final Date[] dates = new Date[] {null};
		final Double[] numbers = new Double[] {null};
		final boolean[][] empty = new boolean[3][5];
		final String[][] strings = new String[3][5];
		TableReaderFactory.getReader(SupportedFile.CSV).as(CsvReader.class).read(
				getInputStream("simple.csv", Charset.forName("Windows-1257")),
				',', '"',
				new TableReaderListener() {
			
			@Override
			public void read(int row, int col) {
				empty[row][col] = true;
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
				dates[row] = value;
			}
			
			@Override
			public void read(int row, int col, String value) {
				//System.out.println(row + "," + col+ ": " + value);
				strings[row][col] = value;
			}

			@Override public void fileStarted(int sheet, String sheetName) {
				fileWorked[0] = true;
				Assert.assertEquals(0, sheet);
				Assert.assertNull(sheetName);
			}

			@Override public void fileFinished(int sheet, String sheetName) {fileWorked[1] = true;  }
			@Override public void rowStart(int row) {}
			@Override public void rowEnd(int row) { }
		});
		Assert.assertEquals(true, fileWorked[1]);
		Assert.assertEquals(true, fileWorked[1]);
		Assert.assertNull(dates[0]);
		Assert.assertNull(numbers[0]);

		Assert.assertEquals(false, readBoolean[0]);
		/*
		char[] chars = strings[0][0].toCharArray();
		for (char c : chars) {
			System.out.print(c);
			System.out.print('=');
			System.out.print((int)c);
			System.out.print(',');
		}
		*/

		Assert.assertNotEquals("Šašikul on kole nälg", strings[0][0]); // �=65533,a=97,�=65533,i=105,k=107,u=117,l=108, =32,o=111,n=110, =32,k=107,o=111,l=108,e=101, =32,n=110,ä=228,l=108,g=103,
		Assert.assertEquals("Tüina", strings[1][0]);
		Assert.assertEquals("Kolmas", strings[2][4]);

	}

	@Test
	public void testTypes() {
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFile.CSV).as(CsvReader.class));
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFile.TXT).as(CsvReader.class));
		for (String extension : new String[] {"csv", "txt"}) {
			Assert.assertNotNull(TableReaderFactory.getReaderByExtendsion(extension).as(CsvReader.class));
			Assert.assertNotNull(TableReaderFactory.getReader("." + extension).as(CsvReader.class));
			for (String name : new String[] {"mina", "see.mina", ""}) {
				Assert.assertNotNull(TableReaderFactory.getReader(name + "." + extension).as(CsvReader.class));
			}
		}
	}
}
