package ee.fj.utils.columnpredictor;

public class ExpectException {
	public static void testException(Class<? extends Throwable> clazz, Runnable runMe) {
		try {
			runMe.run();
		} catch (Throwable t) {
			if (t.getClass() != clazz) {
				throw new AssertionError("Expected " + clazz + " got " + t.getClass());
			}
			return;
		}
		throw new AssertionError("Expected " + clazz);
	}

}
