package com.foxjunior.utils.filetypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FileTypesTest {
	@Test
	@Disabled // This can be run locally as environments might have different types
	public void testPaths() throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileTypesTest.class.getResourceAsStream("/filetypes.txt"), StandardCharsets.UTF_8))) {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (line.length() == 0) continue;
				String[] data = line.split("\t");
				String type = FileTypes.probeContentType(Paths.get("file" + data[0])).orElse(null);

				if (data.length > 2) {
					boolean result = false;
					StringBuilder assertString = new StringBuilder();
					for (int i = 1; i < data.length; i++) {
						if (!result) {
							result = data[i].equals(type);
						}
						if (assertString.length() > 0) assertString.append(" || ");
						assertString.append(data[i]).append(" == ").append(type);
					}
					Assertions.assertTrue(result, data[0] + ": " + assertString.toString());
				} else {
					Assertions.assertEquals( data[1], type, data[0] + " should be " + data[1]);
				}
			}
		}
	}

	@Disabled
	@Test
	public void gatherPaths() throws IOException {
		Path path = Paths.get("J:/");

		Map<String, String> typeExtension = new HashMap<>();

		Files.walkFileTree(path, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				System.out.println(exc.getMessage() + "\t" + file.toString());
				return FileVisitResult.SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) {
				try {
					String name = p.getFileName().toString();
					int extIndex = name.lastIndexOf('.');
					if (extIndex < 0)
						return FileVisitResult.CONTINUE;
					String type = FileTypes.probeContentType(p).orElse(null);
					String ext = name.substring(extIndex);
					if (!type.equals("application/octet-stream")) {
						if (typeExtension.containsKey(ext) && !typeExtension.get(ext).equals(type)) {
							System.out.println("ERR!");
						}
						typeExtension.put(ext, type);
					}
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
				return FileVisitResult.CONTINUE;
			}
		});
		for (Map.Entry<String, String> e : typeExtension.entrySet()) {
			System.out.println(e.getKey() + "\t" + e.getValue());
		}
	}
}
