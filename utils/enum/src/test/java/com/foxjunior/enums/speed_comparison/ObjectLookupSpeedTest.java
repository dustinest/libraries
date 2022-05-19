package com.foxjunior.enums.speed_comparison;

import com.foxjunior.enums.ObjectLookupBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class ObjectLookupSpeedTest {
	static class MyObject {
		public final String value;
		public MyObject(String value) {
			this.value = value;
		}
	}
	static final MyObject[] objects;
	static {
		final char[] chars = "abcdefghijklmnopqrstuvõäöüxyzABCDEFGHIJKLMNOPQRSTUVÕÄÖÜXYZ".toCharArray();
		final StringBuilder builder = new StringBuilder();
		final Collection<MyObject> data = new ArrayList<>();
		for (int i = 0; i < chars.length; i++) {
			builder.append(chars[i]);
			data.add(new MyObject(builder.toString()));
			for (int v = i; v < chars.length; v++) {
				builder.append(chars[v]);
				data.add(new MyObject(builder.toString()));
			}
			builder.setLength(0);
		}
		objects = data.toArray(new MyObject[0]);
	}

	private static void testBuilding() {
		long arrayTotal = 0;
		long objectTotal = 0;
		long start;
		for (int i = 0; i < 50; i++) {
			start = System.currentTimeMillis();
			ObjectLookupArrayBuilder.build(e -> e.value, objects);
			if (i > 0) arrayTotal += System.currentTimeMillis() - start;
			start = System.currentTimeMillis();
			ObjectLookupBuilder.build(e -> e.value, objects);
			if (i > 0) objectTotal += System.currentTimeMillis() - start;
		}

		for (int i = 0; i < 50; i++) {
			start = System.currentTimeMillis();
			ObjectLookupBuilder.build(e -> e.value, objects);
			if (i > 0) objectTotal += System.currentTimeMillis() - start;

			start = System.currentTimeMillis();
			ObjectLookupArrayBuilder.build(e -> e.value, objects);
			if (i > 0) arrayTotal += System.currentTimeMillis() - start;
		}

		System.out.println("ObjectLookupBuilder.build " + objectTotal);
		System.out.println("ObjectLookupArrayBuilder.build " + arrayTotal);
	}

	private static long search(final Function<String, Optional<MyObject>> lookup) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i ++) {
			for (MyObject o : objects) {
				//noinspection OptionalGetWithoutIsPresent
				assert lookup.apply(o.value).get().value.equals(o.value);
			}
		}
		return System.currentTimeMillis() - start;
	}

	public static void main(String... args) {
		testBuilding();

		final Function<String, Optional<MyObject>> objectLookup = ObjectLookupBuilder.build(e -> e.value, objects);
		final Function<String, Optional<MyObject>> arrayLookup = ObjectLookupArrayBuilder.build(e -> e.value, objects);

		search(objectLookup);
		search(arrayLookup);

		long array = 0;
		for (int i = 0; i < 10; i++) {
			array += search(arrayLookup);
		}
		long object = 0;
		for (int i = 0; i < 10; i++) {
			object += search(objectLookup);
		}

		for (int i = 0; i < 10; i++) {
			array += search(arrayLookup);
		}
		for (int i = 0; i < 10; i++) {
			object += search(objectLookup);
		}

		System.out.println("ObjectLookupBuilder " + object);
		System.out.println("ObjectLookupArrayBuilder " + array);

	}
}
