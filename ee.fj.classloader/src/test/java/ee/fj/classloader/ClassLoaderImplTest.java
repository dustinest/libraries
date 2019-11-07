package ee.fj.classloader;

import java.io.IOException;

import org.junit.Test;

import ee.fj.classloader.annotation.OnLoad;

public class ClassLoaderImplTest {
	@OnLoad
	public void annotated() {
		System.out.println("YES");
	}

	@OnLoad
	public static void annotated2() {
		System.out.println("YES2!");
	}

	@OnLoad
	private void annotated3() {
		System.out.println("YES3!");
	}

	@OnLoad
	private static void annotated4() {
		System.out.println("YES1!");
	}


	@Test
	public void testClassLoader() throws IOException {
		ClassLoaderImpl test = new ClassLoaderImpl(Thread.currentThread().getContextClassLoader());
		test.walk(p -> System.out.println(p.toString()));
	}
}
