package com.foxjunior.classloader;

import com.foxjunior.classloader.annotation.OnLoad;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClassLoaderImplTest {
	@OnLoad
	public void annotated() {
		ClassLoaderFactoryTest.annotated1 = true;
		System.out.println("YES");
	}

	@OnLoad
	public static void annotated2() {
		ClassLoaderFactoryTest.annotated2 = true;
		System.out.println("YES2!");
	}

	@OnLoad
	private void annotated3() {
		ClassLoaderFactoryTest.annotated3 = true;
		System.out.println("YES3!");
	}

	@OnLoad
	private static void annotated4() {
		ClassLoaderFactoryTest.annotated4 = true;
		System.out.println("YES1!");
	}


	@Test
	public void testClassLoader() throws IOException {
		ClassLoaderImpl test = new ClassLoaderImpl(Thread.currentThread().getContextClassLoader());
		test.walk(p -> System.out.println(p.toString()));
	}
}
