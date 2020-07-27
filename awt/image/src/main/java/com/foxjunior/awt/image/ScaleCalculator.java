package com.foxjunior.awt.image;

import java.util.Objects;
import java.util.function.BiFunction;

public class ScaleCalculator {
	private static final double ZERO = 0d;
	private static final double ONE= 1d;

	private static <T> T calculateScaleDimension(Number width, Number height, double scale, BiFunction<Number, Number, T> callback) {
		if (scale == 0) callback.apply(ZERO, ZERO);
		if (scale == 1) callback.apply(width, height);
		return callback.apply(width.doubleValue() * scale, height.doubleValue() * scale);
	}

	/**
	 * Scale dimensions to target dimension
	 * @param sourceWidth the width of the first dimension
	 * @param sourceHeight the height of the first dimension
	 * @param targetWidth the width of the second dimension
	 * @param targetHeight the height of the second dimension
	 * @param callback the foruma to calculate the result
	 * @return dimensions based on second dimension
	 */
	public static <T> T targetScale(Number sourceWidth, Number sourceHeight, Number targetWidth, Number targetHeight, BiFunction<Number, Number, T> callback) {
		double scale = getSmallestScale(sourceWidth, sourceHeight, targetWidth,  targetHeight);
		return calculateScaleDimension(sourceWidth, sourceHeight, scale, callback);
	}

	/**
	 * Scale dimensions to source dimension
	 * @param sourceWidth the width of the first dimension
	 * @param sourceHeight the height of the first dimension
	 * @param targetWidth the width of the second dimension
	 * @param targetHeight the height of the second dimension
	 * @param callback the foruma to calculate the result
	 * @return dimensions based on first dimension
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <T> T sourceScale(Number sourceWidth, Number sourceHeight, Number targetWidth, Number targetHeight, BiFunction<Number, Number, T> callback) {
		double scale = getSmallestScale(targetWidth,  targetHeight, sourceWidth, sourceHeight);
		return calculateScaleDimension(targetWidth, targetHeight, scale, callback);
	}

	/**
	 * Calculate scale of two dimensions and return value based on callback
	 * @param sourceWidth the width of the first dimension
	 * @param sourceHeight the height of the first dimension
	 * @param targetWidth the width of the second dimension
	 * @param targetHeight the height of the second dimension
	 * @param callback the foruma to calculate the result
	 * @return callback value of the two scales
	 */
	public static <T extends Number> T getScales(Number sourceWidth, Number sourceHeight, Number targetWidth, Number targetHeight, BiFunction<Double, Double, T> callback) {
		Objects.requireNonNull(sourceWidth, "The width of the first dimension is not nullable!");
		Objects.requireNonNull(sourceHeight, "The height of the first dimension is not nullable!");
		Objects.requireNonNull(targetWidth, "The width of the second dimension is not nullable!");
		Objects.requireNonNull(targetHeight, "The height of the second dimension is not nullable!");
		double scale1 = getScale(sourceWidth.doubleValue(), targetWidth.doubleValue());
		double scale2 = getScale(sourceHeight.doubleValue(), targetHeight.doubleValue());
		return callback.apply(scale1, scale2);
	}

	/**
	 * Calculate smallest scale
	 * @param sourceWidth the width of the first dimension
	 * @param sourceHeight the height of the first dimension
	 * @param targetWidth the width of the second dimension
	 * @param targetHeight the height of the second dimension
	 * @return smallest scale of two dimensions
	 */
	public static double getSmallestScale(Number sourceWidth, Number sourceHeight, Number targetWidth, Number targetHeight) {
		return getScales(sourceWidth, sourceHeight, targetWidth, targetHeight, (scale1, scale2) -> reverse(Math.max(scale1, scale2)));
	}

	/**
	 * Calculate largest scale
	 * @param sourceWidth the width of the first dimension
	 * @param sourceHeight the height of the first dimension
	 * @param targetWidth the width of the second dimension
	 * @param targetHeight the height of the second dimension
	 * @return largest scale of two dimensions
	 */
	public static double getLargestScale(Number sourceWidth, Number sourceHeight, Number targetWidth, Number targetHeight) {
		return getScales(sourceWidth, sourceHeight, targetWidth, targetHeight, (scale1, scale2) -> reverse(Math.min(scale1, scale2)));
	}

	private static double reverse(double amount) {
		if (amount == ZERO) {
			return ZERO;
		}
		if (amount == ONE) {
			return ONE;
		}
		return ONE / amount;
	}

	private static double getScale(double width, double height) {
		if (width == height) { return ONE; }
		if (width == ZERO || height == ZERO) { return ZERO; }
		return width / height;
	}
}
