package ee.fj.io.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class ConfigFileTest {

	@Test
	public void testIo() throws IOException {
		Object[] line1 = new Object[] { "Lorem", true, false, 1l, 2.5d, 3.5f, 4, null, Calendar.getInstance().getTime()};
		Object[] line2 = new Object[] { "Ipsum", false, true, 2l, 3.5d, 4.5f, 5, Calendar.getInstance().getTime(), null};
		int version = 25;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
			writer.write(line1);
			writer.write(line2);
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		try (ConfigFileReader reader = new ConfigFileReader(in)) {
			Assert.assertEquals(version, reader.getVersion());
			Assert.assertArrayEquals(line1, reader.read());
			Assert.assertArrayEquals(line2, reader.read());
			Assert.assertNull(reader.read());
		}
	}

	@Test
	public void moreData() throws IOException {
		Object[] line1 = new Object[] { "Lorem", true, false, 1l, 2.5d, 3.5f, 4, null, Calendar.getInstance().getTime()};
		Object[] line2 = new Object[] { "Ipsum", false, true, 2l, 3.5d, 4.5f, 5, Calendar.getInstance().getTime(), null};
		Object[][] dataToWrite = new Object[10000][];
		Random rnd = new Random();
		for (int i = 0; i < dataToWrite.length; i++) {
			if (rnd.nextInt(2) == 0) {
				dataToWrite[i] = line1;
			} else {
				dataToWrite[i] = line2;
			}
		}
		int version = 25;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
			for (Object[] data : dataToWrite) {
				writer.write(data);
			}
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		try (ConfigFileReader reader = new ConfigFileReader(in)) {
			int i = 0;
			for (Object[] data :reader ) {
				Assert.assertEquals(dataToWrite[i].length, data.length);
				Assert.assertArrayEquals(dataToWrite[i], data);
				i++;
			}
		}
	}
}
