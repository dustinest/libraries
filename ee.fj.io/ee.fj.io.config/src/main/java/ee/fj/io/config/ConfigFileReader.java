package ee.fj.io.config;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigFileReader implements Iterable<Object[]>, Closeable, AutoCloseable {
	private final static Logger LOGGER = Logger.getLogger(ConfigFileReader.class.getName());
	private final InputStream inputStream;
	private final int version;

	public ConfigFileReader(InputStream inputStream) throws IOException {
		this.inputStream = inputStream instanceof BufferedInputStream ? inputStream : new BufferedInputStream(inputStream);
		byte[] intBytes = new byte[Integer.BYTES];
		inputStream.read(intBytes);
		version = ByteCaster.INTEGER.getValue(intBytes);
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

	public Object[] read() throws IOException {
		byte[] resultSizeBytes = new byte[Integer.BYTES];
		int resultSizeBytesRead = inputStream.read(resultSizeBytes);
		if (resultSizeBytesRead == -1) {
			// file is over
			return null;
		}
		int resultSize = ByteCaster.INTEGER.getValue(resultSizeBytes);
		Object[] rv = new Object[resultSize];
		for (int i = 0; i < rv.length; i++) {
			byte[] typeBytes = new byte[1];
			int typeBytesRead = inputStream.read(typeBytes);
			if (typeBytesRead != typeBytes.length) {
				throw new IOException("Illegal length of type " + typeBytesRead + " != " + typeBytes.length);
			}
			byte type = typeBytes[0];
			if (type == ByteCaster.NULL.type) {
				continue;
			}
			byte[] valueLengthBytes = new byte[Integer.BYTES];
			int valueLengthBytesRead = inputStream.read(valueLengthBytes);
			if (valueLengthBytesRead != valueLengthBytes.length) {
				throw new IOException("Illegal length of value length " + valueLengthBytesRead + " != " + valueLengthBytes.length);
			}
			int valueLength = ByteCaster.INTEGER.getValue(valueLengthBytes);

			byte[] valueBytes = new byte[valueLength];
			int valueBytesRead = inputStream.read(valueBytes);
			if (valueBytesRead != valueBytes.length) {
				throw new IOException("Illegal length of value " + valueBytesRead + " != " + valueBytes.length);
			}
			if (type == ByteCaster.INTEGER.type) {
				rv[i] =  ByteCaster.INTEGER.getValue(valueBytes);
			} else if (type == ByteCaster.STRING.type) {
				rv[i] =  ByteCaster.STRING.getValue(valueBytes);
			} else if (type == ByteCaster.BOOLEAN.type) {
				rv[i] =  ByteCaster.BOOLEAN.getValue(valueBytes);
			} else if (type == ByteCaster.FLOAT.type) {
				rv[i] =  ByteCaster.FLOAT.getValue(valueBytes);
			} else if (type == ByteCaster.LONG.type) {
				rv[i] =  ByteCaster.LONG.getValue(valueBytes);
			} else if (type == ByteCaster.DOUBLE.type) {
				rv[i] =  ByteCaster.DOUBLE.getValue(valueBytes);
			} else if (type == ByteCaster.DATE.type) {
				rv[i] =  ByteCaster.DATE.getValue(valueBytes);
			} else {
				throw new IllegalArgumentException("Type " + type + " not supported!");
			}
		}
		return rv;
	}
	
	@Override
	public Iterator<Object[]> iterator() {
		return new Iterator<Object[]>() {
			private Object[] rv;
			@Override
			public boolean hasNext() {
				try {
					rv = read();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error while reading : " + e.getMessage(), e);
					rv = null;
				}
				return rv != null;
			}

			@Override
			public Object[] next() {
				return rv;
			}
		};
	}

}
