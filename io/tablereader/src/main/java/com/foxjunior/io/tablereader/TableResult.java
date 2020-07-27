package com.foxjunior.io.tablereader;

import java.time.LocalDateTime;

public interface TableResult {
	ResultType<String> STRING = new ResultType<>() {
		@Override
		final public String as(Object o) {
			return o != null ? (String) o : null;
		}
	};

	ResultType<Long> LONG = new ResultType<>() {
		@Override
		final public Long as(Object o) {
			return o != null ? (Long) o : null;
		}
	};

	ResultType<Double> DOUBLE = new ResultType<>() {
		@Override
		final public Double as(Object o) {
			return o != null ? (Double) o : null;
		}
	};

	ResultType<LocalDateTime> DATE = new ResultType<>() {
		@Override
		final public LocalDateTime as(Object o) {
			return o != null ? (LocalDateTime) o : null;
		}
	};

	ResultType<Boolean> BOOLEAN = new ResultType<>() {
		@Override
		final public Boolean as(Object o) {
			return o != null ? (Boolean) o : null;
		}
	};

	ResultType<Object> NULL = new ResultType<>() {
		@Override
		final public Object as(Object o) {
			return o;
		}
	};

	ResultType<Integer> ROW_START = new ResultType<>() {
		@Override
		final public Integer as(Object o) {
			return o != null ? (Integer) o : null;
		}
	};

	ResultType<Integer> ROW_END = new ResultType<>() {
		@Override
		final public Integer as(Object o) {
			return o != null ? (Integer) o : null;
		}
	};

	ResultType<Object> BEGIN = new ResultType<>() {
		@Override
		final public Object as(Object o) {
			return o != null ? (Object) o : null;
		}
	};

	ResultType<Integer> END = new ResultType<>() {
		@Override
		final public Integer as(Object o) {
			return o != null ? (Integer) o : null;
		}
	};

	void read(ResultType<?> type, int row, int col, Object value);
}
