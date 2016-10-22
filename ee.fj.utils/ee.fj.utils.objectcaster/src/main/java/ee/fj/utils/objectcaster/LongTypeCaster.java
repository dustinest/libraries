package ee.fj.utils.objectcaster;

public class LongTypeCaster implements TypeCaster<Long> {

	@Override
	public boolean is(Object value) {
		return value instanceof Long;
	}

	@Override
	public Long as(Object value) {
		return value != null ? (Long)value : null;
	}

}
