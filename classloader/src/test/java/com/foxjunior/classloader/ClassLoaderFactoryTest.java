package com.foxjunior.classloader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClassLoaderFactoryTest {
	static boolean annotated1 = false;
	static boolean annotated2 = false;
	static boolean annotated3 = false;
	static boolean annotated4 = false;
	@Test
	public void doRun() throws IOException {
		ClassLoaderFactory.start();
		/*
		Assertions.assertTrue(annotated1);
		Assertions.assertTrue(annotated2);
		Assertions.assertTrue(annotated3);
		Assertions.assertTrue(annotated4);
		 */
	}
}
