package com.foxjunior.io.config;

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

public class ConfigFileReader implements Iterable<Object>, Closeable, AutoCloseable {
	private final InputStream inputStream;
	private final int version;

	public ConfigFileReader(InputStream inputStream) throws IOException {
		this.inputStream = inputStream instanceof BufferedInputStream ? inputStream : new BufferedInputStream(inputStream);
		byte[] intBytes = new byte[Integer.BYTES];
		//noinspection ResultOfMethodCallIgnored
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

	/**
	 * Read next value
	 */
	public Object readNext() throws IOException {
		ConfigType<?> type = nextType();
		if (type == ConfigTypes.NULL) {
			return null;
		}
		return type.getValue(readBytes());
	}

	/**
	 * Read next value and cast it to type. This might throw an exception if wrong type is provided
	 */
	public <T> T readNext(Class<T> clazz) throws IOException {
		ConfigType<?> type = nextType();
		if (type == ConfigTypes.NULL) {
			return null;
		}
		return type.as(clazz).getValue(readBytes());
	}

	/**
	 * Get next data type
	 */
	private ConfigType<?> nextType() throws IOException {
		int typeByte = inputStream.read();
		if (typeByte < 0) {
			throw new EOFException("File ended!");
		}
		return ConfigTypes.getType((byte)typeByte);
	}

	private byte[] readBytes() throws IOException {
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
		return valueBytes;
	}

	/**
	 * Iterate through the values. number returned are the amount of the values read
	 */
	public long read(BiFunction<ConfigType<?>, byte[], Boolean> callback) throws IOException {
		long dataRead = 0;
		while(true) {
			try {
				ConfigType<?> type = nextType();
				dataRead++;
				if (type == ConfigTypes.NULL) {
					if (!callback.apply(ConfigTypes.NULL, new byte[0])) {
						return dataRead;
					}
					continue;
				}

				if (!callback.apply(type, readBytes())) {
					return dataRead;
				}
			} catch (EOFException e) {
				return dataRead;
			}
		}
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<>() {
			private Object val = null;

			@Override
			public boolean hasNext() {
				try {
					val = ConfigFileReader.this.readNext();
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
