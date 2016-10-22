package ee.fj.utils.objectcaster;

public class IntegerTypeCaster implements TypeCaster<Integer> {

	@Override
	public boolean is(Object value) {
		return value instanceof Integer;
	}

	@Override
	public Integer as(Object value) {
		return value != null ? (Integer)value : null;
	}

}
