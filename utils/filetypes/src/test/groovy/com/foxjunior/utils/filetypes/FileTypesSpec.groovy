package com.foxjunior.utils.filetypes


import spock.lang.Ignore
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

class FileTypesSpec extends Specification {
	@Ignore
	// This can be run locally as environments might have different types
	def "test paths"() {
		expect:
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileTypesSpec.class.getResourceAsStream("/filetypes.txt"), StandardCharsets.UTF_8))) {
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					if (line.length() == 0) continue
					String[] data = line.split("\t")
					String type = FileTypes.probeContentType(Paths.get("file" + data[0])).orElse(null)

					if (data.length > 2) {
						boolean result = false
						StringBuilder assertString = new StringBuilder()
						for (int i = 1; i < data.length; i++) {
							if (!result) {
								result = data[i].equals(type)
							}
							if (assertString.length() > 0) assertString.append(" || ")
							assertString.append(data[i]).append(" == ").append(type)
						}
						assert result
					} else {
						assert data[1] == type
					}
				}
			}
	}

	@Ignore // this needs some improvement
	def "gather paths"() {
		given:
			final Path path = Paths.get("J:/")
			Map<String, String> typeExtension = new HashMap<>()
		when:
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				FileVisitResult visitFileFailed(Path file, IOException exc) {
					System.out.println(exc.getMessage() + "\t" + file.toString())
					return FileVisitResult.SKIP_SUBTREE
				}

				@Override
				FileVisitResult visitFile(Path p, BasicFileAttributes attrs) {
					try {
						String name = p.getFileName().toString()
						int extIndex = name.lastIndexOf('.')
						if (extIndex < 0)
							return FileVisitResult.CONTINUE
						String type = FileTypes.probeContentType(p).orElse(null)
						String ext = name.substring(extIndex)
						if (!type.equals("application/octet-stream")) {
							if (typeExtension.containsKey(ext) && !typeExtension.get(ext).equals(type)) {
								System.out.println("ERR!")
							}
							typeExtension.put(ext, type)
						}
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage())
					}
					return FileVisitResult.CONTINUE
				}
			})
		then:
			for (Map.Entry<String, String> e : typeExtension.entrySet()) {
				System.out.println(e.getKey() + "\t" + e.getValue())
			}
	}
}
