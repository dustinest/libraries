package ee.fj.io.config;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigFileWriter implements Closeable, AutoCloseable {
	private final OutputStream outputStream;

	public ConfigFileWriter(int version, OutputStream outputStream) throws IOException {
		this.outputStream = outputStream;
		outputStream.write(((ConfigTypes<Integer>)ConfigTypes.INTEGER).getBytes(version));
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

	public void write(Object value, Object... others) throws IOException {
		writeInternal(value);
		if (others == null) {
			return;
		}
		writeValues(others);
	}

	public void writeValues(Object[] values) throws IOException {
		if (values == null)
			return;
		for (Object o : values) {
			writeInternal(o);
		}
	}

	private void writeInternal(Object value) throws IOException {
		ConfigTypes<?> caster = (ConfigTypes<?>)ConfigTypes.getType(value);
		outputStream.write(caster.type);
		if (caster == ConfigTypes.NULL) {
			return;
		}
		byte[] _data = caster.getBytesFromOBject(value);
		outputStream.write(((ConfigTypes<Integer>)ConfigTypes.INTEGER).getBytes(_data.length));
		outputStream.write(_data);
	}
}
