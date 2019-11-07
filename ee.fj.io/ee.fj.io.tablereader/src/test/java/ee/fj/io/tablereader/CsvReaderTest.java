package ee.fj.io.tablereader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class CsvReaderTest {
	private final String SASHLIK;
	private final String TYINA;

	public CsvReaderTest() throws IOException {
		try (BufferedReader in =  new BufferedReader(new InputStreamReader(CsvReaderTest.class.getClassLoader().getResourceAsStream("test.txt"), StandardCharsets.UTF_8))) {
			this.SASHLIK = in.readLine();
			this.TYINA = in.readLine();
		}
	}

	@Test
	public void simpleIOTest() throws IOException {
		int[] loadingAmount = {-1, -1, -1};
		try (InputStreamReader in =  new InputStreamReader(CsvReaderTest.class.getClassLoader().getResourceAsStream("simple.csv"), Charset.forName("Windows-1257"))) {
			TableReaderFactory.getReader(SupportedFiles.CSV).as(CsvReader.class).read(in , ',', '"',
					(type, row, col, value) -> {
						loadingAmount[0]++;
						if (row == 0 && col == 0) {
							// This is the problem with š in excel to csv.
							//Assert.assertNotEquals("Šašikul on kole nälg", strings[0][0]); // �=65533,a=97,�=65533,i=105,k=107,u=117,l=108, =32,o=111,n=110, =32,k=107,o=111,l=108,e=101, =32,n=110,ä=228,l=108,g=103,
							System.out.println();
							Assert.assertTrue(value + " ends with " + SASHLIK.substring(3), TableResult.STRING.as(value).endsWith(SASHLIK.substring(3)));
							Assert.assertSame(TableResult.STRING, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 0 && col == 1) {
							Assert.assertEquals("1", value);
							Assert.assertSame(TableResult.STRING, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 0 && col == 2) {
							Assert.assertEquals("21/06/11", value);
							Assert.assertSame(TableResult.STRING, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 0 && col == 3) {
							Assert.assertEquals("21/06/2011 12:31:41", value);
							Assert.assertSame(TableResult.STRING, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 0 && col == 4) {
							Assert.assertNull(value);
							Assert.assertSame(TableResult.NULL, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 1 && col == 0) {
							Assert.assertEquals(TYINA, value);
							Assert.assertSame(TableResult.STRING, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 1 && col == 1) {
							Assert.assertEquals("2.1123", value);
							Assert.assertSame(TableResult.STRING, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 1 && col == 2) {
							Assert.assertNull(value);
							Assert.assertSame(TableResult.NULL, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 2 && col == 0) {
							Assert.assertNull(value);
							Assert.assertSame(TableResult.NULL, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 2 && col == 1) {
							Assert.assertEquals("-1", value);
							Assert.assertSame(TableResult.STRING, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 2 && col == 2) {
							Assert.assertNull(value);
							Assert.assertSame(TableResult.NULL, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 2 && col == 3) {
							Assert.assertNull(value);
							Assert.assertSame(TableResult.NULL, type);
							Assert.assertEquals(row, loadingAmount[1]);
						} else if (row == 2 && col == 4) {
							Assert.assertEquals("Kolmas", value);
							Assert.assertSame(TableResult.STRING, type);
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
		Assert.assertEquals(22, loadingAmount[0]);
		Assert.assertEquals(2, loadingAmount[1]);
		Assert.assertEquals(2, loadingAmount[2]);
	}

	@Test
	public void testTypes() {
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFiles.CSV).as(CsvReader.class));
		Assert.assertNotNull(TableReaderFactory.getReader(SupportedFiles.TXT).as(CsvReader.class));
		for (String extension : new String[] {"csv", "txt"}) {
			Assert.assertNotNull(TableReaderFactory.getReaderByExtendsion(extension).as(CsvReader.class));
			Assert.assertNotNull(TableReaderFactory.getReader("." + extension).as(CsvReader.class));
			for (String name : new String[] {"mina", "see.mina", ""}) {
				Assert.assertNotNull(TableReaderFactory.getReader(name + "." + extension).as(CsvReader.class));
			}
		}
	}
}
