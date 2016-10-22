package ee.fj.utils.objectcaster;

public class StringTypeCaster implements TypeCaster<String> {

	@Override
	public boolean is(Object value) {
		return value instanceof String;
	}

	@Override
	public String as(Object value) {
		return value != null ? value.toString() : null;
	}

}
