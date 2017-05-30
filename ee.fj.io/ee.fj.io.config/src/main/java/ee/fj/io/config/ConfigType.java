package ee.fj.io.config;

public interface ConfigType<T> {
	T getValue(byte[] data);
	public boolean is(Class<?> clazz);
	public <S> ConfigType<S> as(Class<S> clazz);
}
