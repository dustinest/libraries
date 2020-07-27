package com.foxjunior.io.config;

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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigFileTest {
	private static final Object[] LINE1 = new Object[] { "Lorem", true, false, 1L, 2.5d, 3.5f, 4, null, Calendar.getInstance().getTime(), LocalDateTime.now(), ZonedDateTime.now(), LocalDate.now()};
	private static final Object[] LINE2 = new Object[] { "Ipsum", false, true, 2L, 3.5d, 4.5f, 5, Calendar.getInstance().getTime(), null};
	private static final byte[] DATA = new byte[] {0,0,0,1,1,0,0,0,5,108,111,114,101,109,1,0,0,0,5,105,112,115,117,109,3,0,0,0,4,0,0,0,1,3,0,0,0,4,0,0,0,2,4,0,0,0,4,65,112,0,0,0};

	@Test
	public void testWriter() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ConfigFileWriter writer = new ConfigFileWriter(1, out)) {
			writer.write("lorem", "ipsum", 1, 2, 15f, null);
		}
		Assertions.assertArrayEquals(DATA, out.toByteArray());
	}

	@Test
	public void simpleTest() throws IOException {
		ByteArrayInputStream data = new ByteArrayInputStream(DATA);
		try (ConfigFileReader reader = new ConfigFileReader(data)) {
			Assertions.assertEquals(1, reader.getVersion());
			Assertions.assertEquals("lorem", reader.readNext(String.class));
			Assertions.assertEquals("ipsum", reader.readNext());
			Assertions.assertEquals(1, reader.readNext(Integer.class).intValue());
			Assertions.assertEquals(2, reader.readNext());
			Assertions.assertEquals(15f, reader.readNext(Float.class), 0);
			Assertions.assertNull(reader.readNext());

			try {
				Object val = reader.readNext();
				throw new AssertionError("End of file expected but " + val + " found!");
			} catch (IOException e) {
				Assertions.assertEquals(EOFException.class, e.getClass());
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
				Assertions.assertEquals(LINE1[index.getAndIncrement()], o);
			}
			Assertions.assertEquals(index.get(), LINE1.length);
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
			Assertions.assertEquals(version, reader.getVersion());
			long result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.STRING, type);
				Assertions.assertEquals(LINE1[0], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.BOOLEAN, type);
				ConfigType<Boolean> parser = type.as(Boolean.class);
				Assertions.assertTrue(parser.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.BOOLEAN, type);
				Assertions.assertEquals(LINE1[2], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.LONG, type);
				Assertions.assertEquals(LINE1[3], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.DOUBLE, type);
				Assertions.assertEquals(LINE1[4], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.FLOAT, type);
				Assertions.assertEquals(LINE1[5], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.INTEGER, type);
				Assertions.assertEquals(LINE1[6], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.NULL, type);
				Assertions.assertNull(type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);
			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.DATE, type);
				Assertions.assertEquals(LINE1[8], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);

			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.LOCALDATETIME, type);
				Assertions.assertEquals(LINE1[9], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);

			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.ZONEDDATETIME, type);
				Assertions.assertEquals(LINE1[10], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);

			result = reader.read((type, data) -> {
				Assertions.assertEquals(ConfigTypes.LOCALDATE, type);
				Assertions.assertEquals(LINE1[11], type.getValue(data));
				return false;
			});
			Assertions.assertEquals(1, result);

			for (Object o : LINE2) {
				Assertions.assertEquals(o, reader.readNext());
			}
			try {
				Object val = reader.readNext();
				throw new AssertionError("End of file expected but " + val + " found!");
			} catch (IOException e) {
				Assertions.assertEquals(EOFException.class, e.getClass());
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
			long result = reader.read((type, data) -> {
				Assertions.assertEquals(dataToWrite[index.get()], type.getValue(data));
				index.addAndGet(1);
				return true;
			});
			Assertions.assertEquals(dataToWrite.length, result);
		}
	}

}
