package com.foxjunior.classloader;

import com.foxjunior.classloader.annotation.OnLoad;

public class ClassLoaderFactoryTest {
	static boolean annotated1 = false;
	static boolean annotated2 = false;
	static boolean annotated3 = false;
	static boolean annotated4 = false;

	@OnLoad
	public void annotated1() {
		ClassLoaderFactoryTest.annotated1 = true;
	}

	@OnLoad
	public static void annotated2() {
		ClassLoaderFactoryTest.annotated2 = true;
	}

	@OnLoad
	private void annotated3() {
		ClassLoaderFactoryTest.annotated3 = true;
	}

	@OnLoad
	private static void annotated4() {
		ClassLoaderFactoryTest.annotated4 = true;
	}
}
