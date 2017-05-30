package ee.fj.io.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class ConfigFileTest {
	private static final Object[] LINE1 = new Object[] { "Lorem", true, false, 1l, 2.5d, 3.5f, 4, null, Calendar.getInstance().getTime(), LocalDateTime.now(), ZonedDateTime.now(), LocalDate.now()};
	private static final Object[] LINE2 = new Object[] { "Ipsum", false, true, 2l, 3.5d, 4.5f, 5, Calendar.getInstance().getTime(), null};
	private static final byte[] DATA = new byte[] {0,0,0,1,1,0,0,0,5,108,111,114,101,109,1,0,0,0,5,105,112,115,117,109,3,0,0,0,4,0,0,0,1,3,0,0,0,4,0,0,0,2,4,0,0,0,4,65,112,0,0,0};

	@Test
	public void testWriter() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ConfigFileWriter writer = new ConfigFileWriter(1, out)) {
			writer.write("lorem", "ipsum", 1, 2, 15f, null);
		}
		Assert.assertArrayEquals(DATA, out.toByteArray());
	}
	
	@Test
	public void simpleTest() throws IOException {
		ByteArrayInputStream data = new ByteArrayInputStream(DATA);
		try (ConfigFileReader reader = new ConfigFileReader(data)) {
			Assert.assertEquals(1, reader.getVersion());
			Assert.assertEquals("lorem", reader.read(String.class));
			Assert.assertEquals("ipsum", reader.read());
			Assert.assertEquals(1, reader.read(Integer.class).intValue());
			Assert.assertEquals(2, reader.read());
			Assert.assertEquals(15f, reader.read(Float.class).floatValue(), 0);
			Assert.assertNull(reader.read());

			try {
				Object val = reader.read();
				throw new AssertionError("End of file expected but " + val + " found!");
			} catch (IOException e) {
				Assert.assertEquals(EOFException.class, e.getClass());
			}

		}
	}

	@Test
	public void iteratorTest() throws IOException {
		int version = 25;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
			writer.writeValues(LINE1);
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		try (ConfigFileReader reader = new ConfigFileReader(in)) {
			AtomicInteger index = new AtomicInteger(0);
			for (Object o : reader) {
				Assert.assertEquals(LINE1[index.getAndIncrement()], o);
			}
			Assert.assertEquals(index.get(), LINE1.length);
		}
	}
	
	@Test
	public void testIo() throws IOException {
		int version = 25;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
			for (Object o : LINE1) {
				writer.write(o);
			}
			writer.writeValues(LINE2);
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		try (ConfigFileReader reader = new ConfigFileReader(in)) {
			Assert.assertEquals(version, reader.getVersion());
			int result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.STRING, type);
				Assert.assertEquals(LINE1[0], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.BOOLEAN, type);
				ConfigType<Boolean> parser = type.as(Boolean.class);
				Assert.assertTrue(parser.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.BOOLEAN, type);
				Assert.assertEquals(LINE1[2], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.LONG, type);
				Assert.assertEquals(LINE1[3], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.DOUBLE, type);
				Assert.assertEquals(LINE1[4], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.FLOAT, type);
				Assert.assertEquals(LINE1[5], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.INTEGER, type);
				Assert.assertEquals(LINE1[6], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.NULL, type);
				Assert.assertNull(type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.DATE, type);
				Assert.assertEquals(LINE1[8], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);

			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.LOCALDATETIME, type);
				Assert.assertEquals(LINE1[9], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);

			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.ZONEDDATETIME, type);
				Assert.assertEquals(LINE1[10], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);

			result = reader.read((type, data) -> {
				Assert.assertEquals(ConfigTypes.LOCALDATE, type);
				Assert.assertEquals(LINE1[11], type.getValue(data));
				return false;
			});
			Assert.assertEquals(1, result);

			for (Object o : LINE2) {
				Assert.assertEquals(o, reader.read());
			}
			try {
				Object val = reader.read();
				throw new AssertionError("End of file expected but " + val + " found!");
			} catch (IOException e) {
				Assert.assertEquals(EOFException.class, e.getClass());
			}
		}
	}

	@Test
	public void moreData() throws IOException {
		Object[] dataToWrite = new Object[100000];
		int version = 25;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
			Random rnd = new Random();
			for (int i = 0; i < dataToWrite.length; i++) {
				dataToWrite[i] = LINE1[rnd.nextInt(LINE1.length)];
				writer.write(dataToWrite[i]);
			}
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		try (ConfigFileReader reader = new ConfigFileReader(in)) {
			AtomicInteger index = new AtomicInteger(0);
			int result = reader.read((type, data) -> {
				Assert.assertEquals(dataToWrite[index.get()], type.getValue(data));
				index.addAndGet(1);
				return true;
			});
			Assert.assertEquals(dataToWrite.length, result);
		}
	}

}
