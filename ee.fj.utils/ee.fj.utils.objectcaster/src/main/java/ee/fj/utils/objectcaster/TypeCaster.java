package ee.fj.utils.objectcaster;

public interface TypeCaster<T> {
	public boolean is(Object value);
	public T as(Object value);
}
