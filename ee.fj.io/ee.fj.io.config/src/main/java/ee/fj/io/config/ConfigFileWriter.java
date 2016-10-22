package ee.fj.io.config;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class ConfigFileWriter implements Closeable, AutoCloseable {
	private final OutputStream outputStream;

	public ConfigFileWriter(int version, OutputStream outputStream) throws IOException {
		this.outputStream = outputStream;
		outputStream.write(ByteCaster.INTEGER.getBytes(version));
	}

	public ConfigFileWriter(int version, Path path) throws IOException {
		this(version, new BufferedOutputStream(Files.newOutputStream(path)));
	}

	public ConfigFileWriter(OutputStream outputStream) throws IOException {
		this(0, outputStream);
	}
	
	public ConfigFileWriter(Path path) throws IOException {
		this(0, path);
	}
	
	@Override
	public void close() throws IOException {
		outputStream.flush();
		outputStream.close();
	}

	public void write(Object... data) throws IOException {
		if (data == null) {
			outputStream.write(ByteCaster.INTEGER.getBytes(0));
			return;
		}
		outputStream.write(ByteCaster.INTEGER.getBytes(data.length));
		for (int i = 0; i < data.length; i++) {
			byte[] dataToWrite = null;
			if (data[i] == null) {
				outputStream.write(ByteCaster.NULL.type);
				continue;
			} else if (data[i] instanceof Integer) {
				outputStream.write(ByteCaster.INTEGER.type);
				dataToWrite = ByteCaster.INTEGER.getBytes(Integer.class.cast(data[i]));
			} else if (data[i] instanceof CharSequence) {
				outputStream.write(ByteCaster.STRING.type);
				dataToWrite = ByteCaster.STRING.getBytes(data[i].toString());
			} else if (data[i] instanceof Boolean) {
				outputStream.write(ByteCaster.BOOLEAN.type);
				dataToWrite = ByteCaster.BOOLEAN.getBytes(Boolean.class.cast(data[i]));
			} else if (data[i] instanceof Float) {
				outputStream.write(ByteCaster.FLOAT.type);
				dataToWrite = ByteCaster.FLOAT.getBytes(Float.class.cast(data[i]));
			} else if (data[i] instanceof Long) {
				outputStream.write(ByteCaster.LONG.type);
				dataToWrite = ByteCaster.LONG.getBytes(Long.class.cast(data[i]));
			} else if (data[i] instanceof Double) {
				outputStream.write(ByteCaster.DOUBLE.type);
				dataToWrite = ByteCaster.DOUBLE.getBytes(Double.class.cast(data[i]));
			} else if (data[i] instanceof Date) {
				outputStream.write(ByteCaster.DATE.type);
				dataToWrite = ByteCaster.DATE.getBytes(Date.class.cast(data[i]));
			} else {
				throw new IllegalArgumentException("Type " + data[i].getClass().getName() + " not supported!");
			}
			outputStream.write(ByteCaster.INTEGER.getBytes(dataToWrite.length));
			outputStream.write(dataToWrite);
		}
	}
}
