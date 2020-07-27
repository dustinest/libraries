package com.foxjunior.utils.filetypes;

import com.sun.activation.registries.MimeTypeFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

public class FileTypes {
	private static final Logger LOGGER = Logger.getLogger(FileTypes.class.getName());
	private static final MimetypesFileTypeMap FILE_TYPES = new MimetypesFileTypeMap();
	//http://svn.apache.org/repos/asf/httpd/httpd/trunk/docs/conf/mime.types
	private static final MimeTypeFile MIME_TYPE_FILE = read("mime.types");
	private static final MimeTypeFile ADDITIONAL_MIME_TYPE = read("additional.mime.types");

	private static MimeTypeFile read(String name) {
		try {
			try (InputStream in = FileTypes.class.getResourceAsStream(name)){
				return new MimeTypeFile(in);
			}
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Error while reading mime types", t);
		}
		return null;
	}

	private static Optional<String> getFromLocalDatabase(Path path) {
		if (path == null) return Optional.empty();
		String fileName = path.getFileName().toString();
		if (fileName == null) return Optional.empty();
		if (!fileName.contains(".")) return Optional.empty();
		int index = fileName.lastIndexOf(".") + 1;
		if (index >= fileName.length()) return Optional.empty();
		fileName = fileName.substring(index);
		if (fileName == null || fileName.length() == 0 ) return Optional.empty();
		final String fileSuffix = fileName.toLowerCase();
		return Optional.ofNullable(MIME_TYPE_FILE.getMIMETypeString(fileSuffix)).or(() -> Optional.ofNullable(ADDITIONAL_MIME_TYPE.getMIMETypeString(fileSuffix)));
	}

	public static Optional<String> probeContentType(Path path) throws IllegalArgumentException {
		return getFromLocalDatabase(path).or(() -> {
			try {
				return Optional.ofNullable(Files.probeContentType(path));
			} catch (IOException e) {
				return Optional.empty();
			}
		});
	}
}
