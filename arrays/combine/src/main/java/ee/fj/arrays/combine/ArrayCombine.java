package ee.fj.arrays.combine;

public class ArrayCombine {
	@SafeVarargs
	public static <T> T[] combine(final T[] first, final T[] second, final T[]... others) {
		int length = 0;
		Class<?> type = null;
		if (first != null) {
			length += first.length;
			type = first.getClass();
		}
		if (second != null) {
			length += second.length;
			if (type == null) type = second.getClass();
		}
		for (T[] o : others) {
			if (o != null) length += o.length;
			if (type == null && o != null) type = o.getClass();
		}
		if (type == null) return null;

		@SuppressWarnings("unchecked")
		T[] result = (T[]) java.lang.reflect.Array.newInstance(type.getComponentType(), length);

		int pos = 0;
		if (first != null) {
			System.arraycopy(first, 0, result, pos, first.length);
			pos += first.length;
		}
		if (second != null) {
			System.arraycopy(second, 0, result, pos, second.length);
			pos += second.length;
		}
		for (T[] o : others) {
			if (o == null) continue;
			System.arraycopy(o, 0, result, pos, o.length);
			pos += o.length;
		}
		return result;
	}

	public static int[] combine(final int[] first, final int[] second, final int[]... others) {
		int length = 0;
		if (first != null) length += first.length;
		if (second != null) length += second.length;
		for (int[] o : others) {
			if (o != null) length += o.length;
		}
		if (length == 0) return null;
		int[] result = new int[length];
		int pos = 0;
		if (first != null) {
			System.arraycopy(first, 0, result, pos, first.length);
			pos += first.length;
		}
		if (second != null) {
			System.arraycopy(second, 0, result, pos, second.length);
			pos += second.length;
		}
		for (int[] o : others) {
			if (o == null) continue;
			System.arraycopy(o, 0, result, pos, o.length);
			pos += o.length;
		}
		return result;
	}

	public static byte[] combine(final byte[] first, final byte[] second, final byte[]... others) {
		int length = 0;
		if (first != null) {
			length += first.length;
		}
		if (second != null) {
			length += second.length;
		}
		for (byte[] o : others) {
			if (o != null) length += o.length;
		}
		if (length == 0) return null;
		byte[] result = new byte[length];
		int pos = 0;
		if (first != null) {
			System.arraycopy(first, 0, result, pos, first.length);
			pos += first.length;
		}
		if (second != null) {
			System.arraycopy(second, 0, result, pos, second.length);
			pos += second.length;
		}
		for (byte[] o : others) {
			if (o == null) continue;
			System.arraycopy(o, 0, result, pos, o.length);
			pos += o.length;
		}
		return result;
	}
	public static char[] combine(final char[] first, final char[] second, final char[]... others) {
		int length = 0;
		if (first != null) {
			length += first.length;
		}
		if (second != null) {
			length += second.length;
		}
		for (char[] o : others) {
			if (o != null) length += o.length;
		}
		if (length == 0) return null;
		char[] result = new char[length];
		int pos = 0;
		if (first != null) {
			System.arraycopy(first, 0, result, pos, first.length);
			pos += first.length;
		}
		if (second != null) {
			System.arraycopy(second, 0, result, pos, second.length);
			pos += second.length;
		}
		for (char[] o : others) {
			if (o == null) continue;
			System.arraycopy(o, 0, result, pos, o.length);
			pos += o.length;
		}
		return result;
	}
	public static boolean[] combine(final boolean[] first, final boolean[] second, final boolean[]... others) {
		int length = 0;
		if (first != null) {
			length += first.length;
		}
		if (second != null) {
			length += second.length;
		}
		for (boolean[] o : others) {
			if (o != null) length += o.length;
		}
		if (length == 0) return null;
		boolean[] result = new boolean[length];
		int pos = 0;
		if (first != null) {
			System.arraycopy(first, 0, result, pos, first.length);
			pos += first.length;
		}
		if (second != null) {
			System.arraycopy(second, 0, result, pos, second.length);
			pos += second.length;
		}
		for (boolean[] o : others) {
			if (o == null) continue;
			System.arraycopy(o, 0, result, pos, o.length);
			pos += o.length;
		}
		return result;
	}

	public static long[] combine(final long[] first, final long[] second, final long[]... others) {
		int length = 0;
		if (first != null) {
			length += first.length;
		}
		if (second != null) {
			length += second.length;
		}
		for (long[] o : others) {
			if (o != null) length += o.length;
		}
		if (length == 0) return null;
		long[] result = new long[length];
		int pos = 0;
		if (first != null) {
			System.arraycopy(first, 0, result, pos, first.length);
			pos += first.length;
		}
		if (second != null) {
			System.arraycopy(second, 0, result, pos, second.length);
			pos += second.length;
		}
		for (long[] o : others) {
			if (o == null) continue;
			System.arraycopy(o, 0, result, pos, o.length);
			pos += o.length;
		}
		return result;
	}

	public static float[] combine(final float[] first, final float[] second, final float[]... others) {
		int length = 0;
		if (first != null) {
			length += first.length;
		}
		if (second != null) {
			length += second.length;
		}
		for (float[] o : others) {
			if (o != null) length += o.length;
		}
		if (length == 0) return null;
		float[] result = new float[length];
		int pos = 0;
		if (first != null) {
			System.arraycopy(first, 0, result, pos, first.length);
			pos += first.length;
		}
		if (second != null) {
			System.arraycopy(second, 0, result, pos, second.length);
			pos += second.length;
		}
		for (float[] o : others) {
			if (o == null) continue;
			System.arraycopy(o, 0, result, pos, o.length);
			pos += o.length;
		}
		return result;
	}
}
