package ee.fj.io.smartreader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EncodingTest {

	private Path getFile(String fileName) throws URISyntaxException {
		return Paths.get(EncodingTest.class.getResource( fileName ).toURI());
	}

	@Test
	public void testUtf8() throws IOException, URISyntaxException {
		try (CharsetAwareInputStream in = Encoding.predict(Files.newInputStream(getFile("utf8.txt")))) {
			Assertions.assertEquals(StandardCharsets.UTF_8, in.getCharset());
		}
	}

	@Test
	public void testUtf16BE() throws IOException, URISyntaxException {
		try (CharsetAwareInputStream in = Encoding.predict(Files.newInputStream(getFile("UTF_16BE.txt")))) {
			MatcherAssert.assertThat(in.getCharset(),
					Matchers.either(Matchers.is(StandardCharsets.UTF_16BE))
							.or(Matchers.is(StandardCharsets.UTF_16)));
		}
	}

	@Test
	public void testUtf16LE() throws IOException, URISyntaxException {
		try (CharsetAwareInputStream in = Encoding.predict(Files.newInputStream(getFile("UTF_16LE.txt")))) {
			Assertions.assertEquals(in.getCharset() , StandardCharsets.UTF_16LE);
		}
	}

	@Test
	public void testWindows1257() throws IOException, URISyntaxException {
		try (CharsetAwareInputStream in = Encoding.predict(getFile("Windows1257.txt"),  Charset.forName("Windows-1257"))) {
			// this might be both as default is enforced
			MatcherAssert.assertThat(in.getCharset(), Matchers.either(Matchers.is(Charset.forName("Windows-1257"))).or(Matchers.is(Charset.defaultCharset())));
		}
	}

	@Test
	public void testWindows1257_utf() throws IOException, URISyntaxException {
		try (CharsetAwareInputStream in = Encoding.predict(getFile("Windows1257.txt"),  StandardCharsets.UTF_16LE)) {
			MatcherAssert.assertThat(in.getCharset(), Matchers.either(Matchers.is(Charset.defaultCharset())).or(Matchers.is(StandardCharsets.UTF_8)));
		}
	}

	@Test
	public void testWindows1257_default() throws IOException, URISyntaxException {
		try (CharsetAwareInputStream in = Encoding.predict(getFile("Windows1257.txt"))) {
			MatcherAssert.assertThat(in.getCharset(), Matchers.either(Matchers.is(Charset.forName("Windows-1257"))).or(Matchers.is(Charset.defaultCharset())));
		}
	}
}
