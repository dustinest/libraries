package ee.fj.awt.image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageResizer {

	/**
	 * Resize image to the size desired. The result image may appear as streched.
	 * @param image Image to resize
	 * @param dimension target size to resize to
	 * @return resized image
	 */
	public static BufferedImage resize(Image image, Dimension dimension) {
		return resize(image, dimension.width, dimension.height);
	}

	/**
	 * Resize image to the size desired. The result image may appear as streched.
	 * @param targetImageType image type to use when creating buffered image
	 * @param image Image to resize
	 * @param dimension target size to resize to
	 * @return resized image
	 */
	public static BufferedImage resize(int targetImageType, Image image, Dimension dimension) {
		return resize(targetImageType, image, dimension.width, dimension.height);
	}

	
	/**
	 * Resize image to the size desired. The result image may appear as streched.
	 * @param image Image to resize
	 * @param width target width
	 * @param height target height
	 * @return resized image
	 */
	public static BufferedImage resize(Image image, Number width, Number height) {
		return resize(solveImageType(image), image, width, height);
	}

	/**
	 * Resize image to the size desired. The result image may appear as streched.
	 * @param targetImageType image type to use when creating buffered image
	 * @param image Image to resize
	 * @param width target width
	 * @param height target height
	 * @return resized image
	 */
	public static BufferedImage resize(int targetImageType, Image image, Number width, Number height) {
		Objects.requireNonNull(width, "The width is not nullable!");
		Objects.requireNonNull(height, "The height is not nullable!");
		BufferedImage resizedImage = new BufferedImage(width.intValue(), height.intValue(), targetImageType);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width.intValue(), height.intValue(), null);
		return resizedImage;
	}

	private static int solveImageType(Image image) {
		int rv = BufferedImage.TYPE_INT_ARGB;
		if (image instanceof BufferedImage) {
			rv = ((BufferedImage)image).getType();
			if (rv == BufferedImage.TYPE_CUSTOM) {
				rv = BufferedImage.TYPE_INT_ARGB;
			}
		}
		return rv;
	}

	/**
	 * Scale image to desired size
	 * The result image might not have desired height or width because of the scale. For instance, if image width is 100 and height is 100 but desired size is 50 x 25 then image will be scaled to 25x25
	 * @param image Image to sicale
	 * @param dimension desired size of the target image
	 * @return scaled image
	 */
	public static BufferedImage scale(Image image, Dimension dimension) {
		return scale(solveImageType(image), image, dimension);
	}


	/**
	 * Scale image to desired size
	 * The result image might not have desired height or width because of the scale. For instance, if image width is 100 and height is 100 but desired size is 50 x 25 then image will be scaled to 25x25
	 * @param targetImageType image type to use when creating buffered image
	 * @param image Image to sicale
	 * @param dimension desired size of the target image
	 * @return scaled image
	 */
	public static BufferedImage scale(int targetImageType, Image image, Dimension dimension) {
		return scale(targetImageType, image, dimension.width, dimension.height);
	}



	/**
	 * Scale image using the scaling factor
	 * @param image image to scale
	 * @param scalingFactor scaling factor to use at image size
	 * @return new scaled image
	 */
	public static BufferedImage scale(Image image, double scalingFactor) {
		return scale(solveImageType(image), image, scalingFactor);
	}

	/**
	 * Scale image using the scaling factor
	 * @param targetImageType image type to use when creating buffered image
	 * @param image image to scale
	 * @param scalingFactor scaling factor to use at image size
	 * @return new scaled image
	 */
	public static BufferedImage scale(int targetImageType, Image image, double scalingFactor) {
		return resize(targetImageType, image,
				(int)Math.floor(image.getWidth(null) * scalingFactor),
				(int)Math.floor(image.getHeight(null) * scalingFactor));
	}

	/**
	 * Scale image to desired size
	 * The result image might not have desired height or width because of the scale. For instance, if image width is 100 and height is 100 but desired size is 50 x 25 then image will be scaled to 25x25
	 * @param image Image to sicale
	 * @param width desired width of the target image
	 * @param height desired height of the target image
	 * @return scaled image
	 */
	public static BufferedImage scale(Image image, Number width, Number height) {
		return scale(solveImageType(image), image, width, height);
	}

	/**
	 * Scale image to desired size
	 * The result image might not have desired height or width because of the scale. For instance, if image width is 100 and height is 100 but desired size is 50 x 25 then image will be scaled to 25x25
	 * @param targetImageType image type to use when creating buffered image
	 * @param image Image to sicale
	 * @param width desired width of the target image
	 * @param height desired height of the target image
	 * @return scaled image
	 */
	public static BufferedImage scale(int targetImageType, Image image, Number width, Number height) {
		return ScaleCalculator.targetScale(image.getWidth(null), image.getHeight(null), width, height, (w, h) -> resize(targetImageType, image, w, h));
	}


}
