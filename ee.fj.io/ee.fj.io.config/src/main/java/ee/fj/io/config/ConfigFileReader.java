package ee.fj.io.config;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConfigFileReader implements Iterable<Object>, Closeable, AutoCloseable {
	private final static Logger LOGGER = Logger.getLogger(ConfigFileReader.class.getName());
	private final InputStream inputStream;
	private final int version;

	public ConfigFileReader(InputStream inputStream) throws IOException {
		this.inputStream = inputStream instanceof BufferedInputStream ? inputStream : new BufferedInputStream(inputStream);
		byte[] intBytes = new byte[Integer.BYTES];
		inputStream.read(intBytes);
		version = ConfigTypes.INTEGER.getValue(intBytes);
	}

	public ConfigFileReader(Path path) throws IOException {
		this(new BufferedInputStream(Files.newInputStream(path)));
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}

	public int getVersion() {
		return version;
	}

	public Object readValue() throws IOException {
		Object[] rv = new Object[]{null};
		int lines = read((type, val) -> {
			rv[0] = type.getValue(val);
			return false;
		});
		if (lines == 0) {
			throw new EOFException("File ended!");
		}
		return rv[0];
	}
	
	public int read(BiFunction<ConfigType<?>, byte[], Boolean> callback) throws IOException {
		int dataRead = 0;
		while(true) {
			int typeByte = inputStream.read();
			if (typeByte < 0) {
				return dataRead;
			}
			dataRead++;
			ConfigType<?> type = ConfigTypes.getType((byte)typeByte);
			if (type == ConfigTypes.NULL) {
				if (!callback.apply(ConfigTypes.NULL, new byte[0])) {
					return dataRead;
				}
				continue;
			}

			byte[] valueLengthBytes = new byte[Integer.BYTES];
			int valueLengthBytesRead = inputStream.read(valueLengthBytes);
			if (valueLengthBytesRead != valueLengthBytes.length) {
				throw new IOException("Illegal length of value length " + valueLengthBytesRead + " != " + valueLengthBytes.length);
			}
			int valueLength = ConfigTypes.INTEGER.getValue(valueLengthBytes);

			byte[] valueBytes = new byte[valueLength];
			int valueBytesRead = inputStream.read(valueBytes);
			if (valueBytesRead != valueBytes.length) {
				throw new IOException("Illegal length of value " + valueBytesRead + " != " + valueBytes.length);
			}
			if (!callback.apply(type, valueBytes)) {
				return dataRead;
			}
		}
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			private Object val = null;
			@Override
			public boolean hasNext() {
				try {
					val = readValue();
					return true;
				} catch (EOFException e) {
					val = null;
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
				return false;
			}

			@Override
			public Object next() {
				return val;
			}
		};
	}
}
