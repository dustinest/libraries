package ee.fj.classloader;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClassLoaderFactoryTest {

	@Test
	public void doRun() throws IOException {
		ClassLoaderFactory.start();
	}
}
