package ee.fj.io.smartreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ListenedFileReader {
	interface FileStartListener { public void startReading(InputStream in, Charset charset) throws IOException; }
	interface FileReaderListener { public void startReading(Charset charset); public void doneReading(Charset charset); public void fileRead(String line); }
	interface FileReadListener { public void fileReadingDone(Charset charset, String[] lines);}

	private final FileStartListener fileListener;
	public ListenedFileReader(FileStartListener listener) {
		this.fileListener = listener;
	}

	public ListenedFileReader(FileReaderListener listener) {
		this.fileListener = new FileStartListener() {
			@Override
			public void startReading(InputStream in, Charset charset) throws IOException {
				listener.startReading(charset);;
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset))) {
					for (String line = reader.readLine(); line != null; line = reader.readLine()) {
						listener.fileRead(line);
					}
				}
			}
		};
	}

	public ListenedFileReader(FileReadListener listener) {
		this(new FileReaderListener() {
			List<String> lines = new ArrayList<>();
			
			@Override
			public void fileRead(String line) {
				lines.add(line);
			}

			@Override
			public void startReading(Charset charset) {
				lines.clear();
			}

			@Override
			public void doneReading(Charset charset) {
				listener.fileReadingDone(charset, lines.toArray(new String[lines.size()]));
			}
		});
	}

	private Path path;
	private Charset charset;
	private boolean isResetFile = Boolean.TRUE;
	public Path getPath() { return path; }
	public Charset getCharset() { return charset; }

	public boolean setPath(Path path) throws IOException {
		this.path = path;
		isResetFile = true;
		return read();
	}

	public boolean setCharset(Charset charset) throws IOException {
		this.charset = charset;
		return read();
	}

	private boolean read() throws IOException {
		if (path == null)
			return false;
		try(CharsetAwareInputStream in = Encoding.predict(path, charset == null || isResetFile ? Charset.defaultCharset() : charset)) {
			fileListener.startReading(in, in.getCharset());
		}
		isResetFile = false;
		return true;
	}
}
