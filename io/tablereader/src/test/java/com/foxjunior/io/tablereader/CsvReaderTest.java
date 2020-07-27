package com.foxjunior.io.tablereader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
							//Assertions.assertNotEquals("Šašikul on kole nälg", strings[0][0]); // �=65533,a=97,�=65533,i=105,k=107,u=117,l=108, =32,o=111,n=110, =32,k=107,o=111,l=108,e=101, =32,n=110,ä=228,l=108,g=103,
							System.out.println();
							Assertions.assertTrue(TableResult.STRING.as(value).endsWith(SASHLIK.substring(3)),value + " ends with " + SASHLIK.substring(3));
							Assertions.assertSame(TableResult.STRING, type);
							Assertions.assertEquals(row, loadingAmount[1]);
						} else if (row == 0 && col == 1) {
							Assertions.assertEquals("1", value);
							Assertions.assertSame(TableResult.STRING, type);
							Assertions.assertEquals(row, loadingAmount[1]);
						} else if (row == 0 && col == 2) {
							Assertions.assertEquals("21/06/11", value);
							Assertions.assertSame(TableResult.STRING, type);
							Assertions.assertEquals(row, loadingAmount[1]);
						} else if (row == 0 && col == 3) {
							Assertions.assertEquals("21/06/2011 12:31:41", value);
							Assertions.assertSame(TableResult.STRING, type);
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
							Assertions.assertEquals("2.1123", value);
							Assertions.assertSame(TableResult.STRING, type);
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
							Assertions.assertEquals("-1", value);
							Assertions.assertSame(TableResult.STRING, type);
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
		Assertions.assertEquals(22, loadingAmount[0]);
		Assertions.assertEquals(2, loadingAmount[1]);
		Assertions.assertEquals(2, loadingAmount[2]);
	}

	@Test
	void testTypes() {
		Assertions.assertNotNull(TableReaderFactory.getReader(SupportedFiles.CSV).as(CsvReader.class));
		Assertions.assertNotNull(TableReaderFactory.getReader(SupportedFiles.TXT).as(CsvReader.class));
		for (String extension : new String[] {"csv", "txt"}) {
			Assertions.assertNotNull(TableReaderFactory.getReaderByExtendsion(extension).as(CsvReader.class));
			Assertions.assertNotNull(TableReaderFactory.getReader("." + extension).as(CsvReader.class));
			for (String name : new String[] {"mina", "see.mina", ""}) {
				Assertions.assertNotNull(TableReaderFactory.getReader(name + "." + extension).as(CsvReader.class));
			}
		}
	}
}
