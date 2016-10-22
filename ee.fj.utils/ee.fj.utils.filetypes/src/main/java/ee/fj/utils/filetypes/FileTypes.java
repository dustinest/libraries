package ee.fj.utils.filetypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

public class FileTypes {
	private static final Logger LOGGER = Logger.getLogger(FileTypes.class.getName());
	private static final MimetypesFileTypeMap FILE_TYPES = new MimetypesFileTypeMap();
	static {
		try {
			try(BufferedReader in = new BufferedReader(new InputStreamReader(
					FileTypes.class.getResourceAsStream("mime.types"), StandardCharsets.UTF_8))) {
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					FILE_TYPES.addMimeTypes(line);
				}
			}
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Error while reading mime types", t);
		}
	}

	public static String probeContentType(Path path) throws IllegalArgumentException {
		try {
			String rv = Files.probeContentType(path);
			if (rv != null) {
				return rv;
			}
			rv = FILE_TYPES.getContentType(path.toFile());
			if (rv != null) {
				return rv;
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Mime type for path " + path + " not found!", e);
		}
		throw new IllegalArgumentException("Mime type for path " + path + " not found!");
	}
}
