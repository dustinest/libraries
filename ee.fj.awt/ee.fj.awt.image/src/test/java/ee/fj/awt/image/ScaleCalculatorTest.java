package ee.fj.awt.image;

import org.junit.Assert;
import org.junit.Test;

public class ScaleCalculatorTest {

	@Test
	public void testGetSmallestScale() {
		Assert.assertEquals(0.5d, ScaleCalculator.getSmallestScale(100, 100, 50, 50), 0);
		Assert.assertEquals(2d, ScaleCalculator.getSmallestScale(50, 50, 100, 100), 0);
		Assert.assertEquals(0.5d, ScaleCalculator.getSmallestScale(100, 50, 50, 25), 0);
		Assert.assertEquals(2d, ScaleCalculator.getSmallestScale(50, 25, 100, 50), 0);
		Assert.assertEquals(0.25d, ScaleCalculator.getSmallestScale(100, 75, 25, 50), 0);
	}

	@Test
	public void testGetLargestScale() {
		Assert.assertEquals(0.5d, ScaleCalculator.getLargestScale(100, 100, 50, 50), 0);
		Assert.assertEquals(2d, ScaleCalculator.getLargestScale(50, 50, 100, 100), 0);
		Assert.assertEquals(0.5d, ScaleCalculator.getLargestScale(100, 50, 50, 25), 0);
		Assert.assertEquals(2d, ScaleCalculator.getLargestScale(50, 25, 100, 50), 0);
		Assert.assertEquals(0.66d, ScaleCalculator.getLargestScale(100, 75, 25, 50), 0.06);
	}

	@Test
	public void testTargetScale() {
		ScaleCalculator.targetScale(100, 100, 50, 50, (w, h) -> {
			Assert.assertEquals(50d, w.doubleValue(), 0);
			Assert.assertEquals(50d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.targetScale(50, 50, 100, 100, (w, h) -> {
			Assert.assertEquals(100d, w.doubleValue(), 0);
			Assert.assertEquals(100d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.targetScale(100, 50, 50, 25, (w, h) -> {
			Assert.assertEquals(50d, w.doubleValue(), 0);
			Assert.assertEquals(25d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.targetScale(25, 50, 50, 100, (w, h) -> {
			Assert.assertEquals(50d, w.doubleValue(), 0);
			Assert.assertEquals(100d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.targetScale(100, 75, 25, 50, (w, h) -> {
			Assert.assertEquals(25d, w.doubleValue(), 0);
			Assert.assertEquals(18.75d, h.doubleValue(), 0);
			return null;
		});
	}

	@Test
	public void testSourceScale() {
		ScaleCalculator.sourceScale(100, 100, 50, 50, (w, h) -> {
			Assert.assertEquals(100d, w.doubleValue(), 0);
			Assert.assertEquals(100d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.sourceScale(50, 50, 100, 100, (w, h) -> {
			Assert.assertEquals(50d, w.doubleValue(), 0);
			Assert.assertEquals(50d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.sourceScale(100, 50, 50, 25, (w, h) -> {
			Assert.assertEquals(100d, w.doubleValue(), 0);
			Assert.assertEquals(50d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.sourceScale(25, 50, 50, 100, (w, h) -> {
			Assert.assertEquals(25d, w.doubleValue(), 0);
			Assert.assertEquals(50d, h.doubleValue(), 0);
			return null;
		});

		ScaleCalculator.sourceScale(100, 75, 25, 50, (w, h) -> {
			Assert.assertEquals(37.5d, w.doubleValue(), 0);
			Assert.assertEquals(75d, h.doubleValue(), 0);
			return null;
		});
	}
}
