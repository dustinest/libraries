package ee.fj.io.config;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public abstract class ByteCaster<T> {
	protected byte type;
	protected ByteCaster(byte type) { this.type = type; }

	public final T getValue(byte[] bytes) {
		if (bytes.length == 0)
			return null;
		return toValueInner(bytes);
	}

	public final byte[] getBytes(T value) {
		if (value == null)
			return new byte[0];
		return toBytesInner(value);
	}

	protected abstract byte[] toBytesInner(T value);
	protected abstract T toValueInner(byte[] data);

	public static final ByteCaster<String> NULL = new ByteCaster<String>((byte)0) {
		@Override protected byte[] toBytesInner(String value) { throw new UnsupportedOperationException("Not implemented"); }
		@Override protected String toValueInner(byte[] bytes) { throw new UnsupportedOperationException("Not implemented"); }
	};

	public static final ByteCaster<String> STRING = new ByteCaster<String>((byte)1) {
		@Override protected byte[] toBytesInner(String value) { return value.getBytes(StandardCharsets.UTF_8); }
		@Override protected String toValueInner(byte[] bytes) { return new String(bytes, StandardCharsets.UTF_8); }
	};

	public static final ByteCaster<Boolean> BOOLEAN = new ByteCaster<Boolean>((byte)2) {
		@Override protected byte[] toBytesInner(Boolean value) {  return  value ? new byte[] {1} : new byte[] {0}; }
		@Override protected Boolean toValueInner(byte[] bytes) { return bytes[0] == 1; }
	};

	public static final ByteCaster<Integer> INTEGER = new ByteCaster<Integer>((byte)3) {
		@Override protected byte[] toBytesInner(Integer value) {  return  ByteBuffer.allocate(Integer.BYTES).putInt(value).array(); }
		@Override protected Integer toValueInner(byte[] bytes) { return ByteBuffer.wrap(bytes).getInt(); }
	};
	public static final ByteCaster<Float> FLOAT = new ByteCaster<Float>((byte)4) {
		@Override protected byte[] toBytesInner(Float value) {  return  ByteBuffer.allocate(Float.BYTES).putFloat(value).array(); }
		@Override protected Float toValueInner(byte[] bytes) { return ByteBuffer.wrap(bytes).getFloat(); }
	};
	public static final ByteCaster<Long> LONG = new ByteCaster<Long>((byte)5) {
		@Override protected byte[] toBytesInner(Long value) {  return  ByteBuffer.allocate(Long.BYTES).putLong(value).array(); }
		@Override protected Long toValueInner(byte[] bytes) { return ByteBuffer.wrap(bytes).getLong(); }
	};
	public static final ByteCaster<Double> DOUBLE = new ByteCaster<Double>((byte)6) {
		@Override protected byte[] toBytesInner(Double value) {  return  ByteBuffer.allocate(Double.BYTES).putDouble(value).array(); }
		@Override protected Double toValueInner(byte[] bytes) { return ByteBuffer.wrap(bytes).getDouble(); }
	};

	public static final ByteCaster<Date> DATE = new ByteCaster<Date>((byte)7) {
		@Override protected byte[] toBytesInner(Date value) {  return  ByteCaster.LONG.getBytes(value.getTime()); }
		@Override protected Date toValueInner(byte[] bytes) { return new Date(ByteCaster.LONG.getValue(bytes)); }
	};
}
