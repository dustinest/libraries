package ee.fj.io.tablereader;

import java.time.LocalDateTime;

public interface TableResult {
	public static final ResultType<String> STRING = new ResultType<String>() {
		@Override final public String as(Object o) { return o != null ? (String)o : null; }
	};

	public static final ResultType<Long> LONG = new ResultType<Long>() {
		@Override final public Long as(Object o) { return o != null ? (Long)o : null; }
	};

	public static final ResultType<Double> DOUBLE = new ResultType<Double>() {
		@Override final public Double as(Object o) { return o != null ? (Double)o : null; }
	};

	public static final ResultType<LocalDateTime> DATE = new ResultType<LocalDateTime>() {
		@Override final public LocalDateTime as(Object o) { return o != null ? (LocalDateTime)o : null; }
	};

	public static final ResultType<Boolean> BOOLEAN = new ResultType<Boolean>() {
		@Override final  public Boolean as(Object o) { return o != null ? (Boolean)o : null; }
	};

	public static final ResultType<Object> NULL = new ResultType<Object>() {
		@Override final public Object as(Object o) { return o; }
	};

	public static final ResultType<Integer> ROW_START = new ResultType<Integer>() {
		@Override final public Integer as(Object o) { return o != null ? (Integer)o : null; }
	};

	public static final ResultType<Integer> ROW_END = new ResultType<Integer>() {
		@Override final public Integer as(Object o) { return o != null ? (Integer)o : null; }
	};

	public static final ResultType<Object> BEGIN = new ResultType<Object>() {
		@Override final public Object as(Object o) { return o != null ? (Object)o : null; }
	};
	
	public static final ResultType<Integer> END = new ResultType<Integer>() {
		@Override final public Integer as(Object o) { return o != null ? (Integer)o : null; }
	};

	public void read(ResultType<?> type, int row, int col, Object value);
}
