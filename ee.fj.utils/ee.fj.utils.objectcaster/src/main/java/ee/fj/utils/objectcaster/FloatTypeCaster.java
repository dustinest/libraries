package ee.fj.utils.objectcaster;

public class FloatTypeCaster  implements TypeCaster<Float> {

	@Override
	public boolean is(Object value) {
		return value instanceof Float;
	}

	@Override
	public Float as(Object value) {
		return value != null ? (Float)value : null;
	}

}
