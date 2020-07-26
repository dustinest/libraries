package ee.fj.arrays.combine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayCombineTest {
	@Test
	public void testTwoIntegers() {
		Assertions.assertArrayEquals(new int[]{1,2,3,4,5}, ArrayCombine.combine(new int[]{1,2, 3}, new int[]{4,5}));
		Assertions.assertArrayEquals(new int[]{1,2,3, 3,4,5,6,7}, ArrayCombine.combine(new int[]{1,2,3}, new int[]{3,4,5,6,7}));
		Assertions.assertArrayEquals(new int[]{1,2}, ArrayCombine.combine(new int[]{1,2}, new int[0]));
		Assertions.assertArrayEquals(new int[]{3,4}, ArrayCombine.combine(new int[0], new int[]{3,4}));
		Assertions.assertArrayEquals(new int[]{1,2}, ArrayCombine.combine(new int[]{1,2}, null));
		Assertions.assertArrayEquals(new int[]{3,4}, ArrayCombine.combine(null, new int[]{3,4}));
	}

	@Test
	public void testNullResult() {
		Assertions.assertNull(ArrayCombine.combine((String[])null, null, null, null));
	}

	@Test
	public void testLastInteger() {
		String[] result = ArrayCombine.combine(null, null, null, new String[]{"1","2","3"});
		Assertions.assertArrayEquals(new String[]{"1","2","3"}, result);
	}

	@Test
	public void testMoreIntegers() {
		int[] result = ArrayCombine.combine(
				new int[]{1,2},
				new int[]{3,4,5,6,7},
				new int[]{8, 9},
				new int[]{10},
				new int[]{11}
		);
		Assertions.assertArrayEquals(new int[]{1,2, 3,4,5,6,7, 8,9, 10, 11}, result);
	}
}
