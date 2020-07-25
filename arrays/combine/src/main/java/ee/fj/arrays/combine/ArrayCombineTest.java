package ee.fj.arrays.combine;

import org.junit.Assert;
import org.junit.Test;

public class ArrayCombineTest {
	@Test
	public void testTwoIntegers() {
		Assert.assertArrayEquals(new int[]{1,2,3,4,5}, ArrayCombine.combine(new int[]{1,2, 3}, new int[]{4,5}));
		Assert.assertArrayEquals(new int[]{1,2,3, 3,4,5,6,7}, ArrayCombine.combine(new int[]{1,2,3}, new int[]{3,4,5,6,7}));
		Assert.assertArrayEquals(new int[]{1,2}, ArrayCombine.combine(new int[]{1,2}, new int[0]));
		Assert.assertArrayEquals(new int[]{3,4}, ArrayCombine.combine(new int[0], new int[]{3,4}));
		Assert.assertArrayEquals(new int[]{1,2}, ArrayCombine.combine(new int[]{1,2}, null));
		Assert.assertArrayEquals(new int[]{3,4}, ArrayCombine.combine(null, new int[]{3,4}));
	}

	@Test
	public void testNullResult() {
		Assert.assertNull(ArrayCombine.combine((String[])null, null, null, null));
	}

	@Test
	public void testLastInteger() {
		String[] result = ArrayCombine.combine((String[])null, null, null, new String[]{"1","2","3"});
		Assert.assertArrayEquals(new String[]{"1","2","3"}, result);
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
		Assert.assertArrayEquals(new int[]{1,2, 3,4,5,6,7, 8,9, 10, 11}, result);
	}
}
