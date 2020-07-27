package com.foxjunior.io.config;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public abstract class ConfigTypes<T> implements ConfigType<T> {
	final byte type;
	private final String name;
	private final Class<T> clazz;
	private ConfigTypes(byte type, Class<T> clazz) {
		this.type = type;
		this.name = clazz == null ? "null" : clazz.getName();
		this.clazz = clazz;
	}

	@Override
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

	@SuppressWarnings("unchecked")
	public final byte[] getBytesFromOBject(Object value) {
		if (value == null)
			return new byte[0];
		return toBytesInner((T)value);
	}
	@Override public String toString() { return name; }
	@Override public boolean is(Class<?> clazz) { return this.clazz.isAssignableFrom(clazz); }

	abstract byte[] toBytesInner(T value);
	abstract T toValueInner(byte[] data);

	@SuppressWarnings("unchecked")
	@Override
	public <S> ConfigType<S> as(Class<S> clazz) {
		return (ConfigType<S>)this;
	}

	public static final ConfigType<String> NULL = new ConfigTypes<>((byte) 0, null) {
		@Override
		byte[] toBytesInner(String value) {
			throw new UnsupportedOperationException("Not implemented");
		}

		@Override
		String toValueInner(byte[] bytes) {
			throw new UnsupportedOperationException("Not implemented");
		}
	};

	public static final ConfigType<CharSequence> STRING = new ConfigTypes<>((byte) 1, CharSequence.class) {
		@Override
		byte[] toBytesInner(CharSequence value) {
			return value.toString().getBytes(StandardCharsets.UTF_8);
		}

		@Override
		CharSequence toValueInner(byte[] bytes) {
			return new String(bytes, StandardCharsets.UTF_8);
		}
	};

	public static final ConfigType<Boolean> BOOLEAN = new ConfigTypes<>((byte) 2, Boolean.class) {
		@Override
		byte[] toBytesInner(Boolean value) {
			return value ? new byte[]{1} : new byte[]{0};
		}

		@Override
		Boolean toValueInner(byte[] bytes) {
			return bytes[0] == 1;
		}
	};

	public static final ConfigType<Integer> INTEGER = new ConfigTypes<>((byte) 3, Integer.class) {
		@Override
		byte[] toBytesInner(Integer value) {
			return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
		}

		@Override
		Integer toValueInner(byte[] bytes) {
			return ByteBuffer.wrap(bytes).getInt();
		}
	};
	public static final ConfigType<Float> FLOAT = new ConfigTypes<>((byte) 4, Float.class) {
		@Override
		byte[] toBytesInner(Float value) {
			return ByteBuffer.allocate(Float.BYTES).putFloat(value).array();
		}

		@Override
		Float toValueInner(byte[] bytes) {
			return ByteBuffer.wrap(bytes).getFloat();
		}
	};
	public static final ConfigType<Long> LONG = new ConfigTypes<>((byte) 5, Long.class) {
		@Override
		byte[] toBytesInner(Long value) {
			return ByteBuffer.allocate(Long.BYTES).putLong(value).array();
		}

		@Override
		Long toValueInner(byte[] bytes) {
			return ByteBuffer.wrap(bytes).getLong();
		}
	};
	public static final ConfigType<Double> DOUBLE = new ConfigTypes<>((byte) 6, Double.class) {
		@Override
		byte[] toBytesInner(Double value) {
			return ByteBuffer.allocate(Double.BYTES).putDouble(value).array();
		}

		@Override
		Double toValueInner(byte[] bytes) {
			return ByteBuffer.wrap(bytes).getDouble();
		}
	};

	public static final ConfigType<Date> DATE = new ConfigTypes<>((byte) 7, Date.class) {
		@Override
		byte[] toBytesInner(Date value) {
			return ((ConfigTypes<Long>) ConfigTypes.LONG).getBytes(value.getTime());
		}

		@Override
		Date toValueInner(byte[] bytes) {
			return new Date(ConfigTypes.LONG.getValue(bytes));
		}
	};

	public static final ConfigType<LocalDate> LOCALDATE = new ConfigTypes<>((byte) 8, LocalDate.class) {
		@Override
		byte[] toBytesInner(LocalDate value) {
			return ((ConfigTypes<CharSequence>) ConfigTypes.STRING).getBytes(value.toString());
		}

		@Override
		LocalDate toValueInner(byte[] bytes) {
			return LocalDate.parse(ConfigTypes.STRING.getValue(bytes));
		}
	};

	public static final ConfigType<LocalDateTime> LOCALDATETIME = new ConfigTypes<>((byte) 9, LocalDateTime.class) {
		@Override
		protected byte[] toBytesInner(LocalDateTime value) {
			return ((ConfigTypes<CharSequence>) ConfigTypes.STRING).getBytes(value.toString());
		}

		@Override
		protected LocalDateTime toValueInner(byte[] bytes) {
			return LocalDateTime.parse(ConfigTypes.STRING.getValue(bytes));
		}
	};

	public static final ConfigType<ZonedDateTime> ZONEDDATETIME = new ConfigTypes<>((byte) 10, ZonedDateTime.class) {
		@Override
		byte[] toBytesInner(ZonedDateTime value) {
			return ((ConfigTypes<CharSequence>) ConfigTypes.STRING).getBytes(value.toString());
		}

		@Override
		ZonedDateTime toValueInner(byte[] bytes) {
			return ZonedDateTime.parse(ConfigTypes.STRING.getValue(bytes));
		}
	};

	private static final ConfigType<?>[] TYPES = new ConfigType<?>[]{
		ConfigTypes.STRING,
		ConfigTypes.BOOLEAN,
		ConfigTypes.INTEGER,
		ConfigTypes.FLOAT,
		ConfigTypes.LONG,
		ConfigTypes.DOUBLE,
		ConfigTypes.DATE,
		ConfigTypes.LOCALDATE,
		ConfigTypes.LOCALDATETIME,
		ConfigTypes.ZONEDDATETIME

	};

	public static ConfigType<?> getType(byte type) {
		if (type == ((ConfigTypes<?>)ConfigTypes.NULL).type) {
			return ConfigTypes.NULL;
		}
		for (ConfigType<?> c : TYPES) {
			if (((ConfigTypes<?>) c).type == type) {
				return c;
			}
		}
		throw new IllegalArgumentException("Type " + type + " not found!");
	}

	public static ConfigType<?> getType(Object data) {
		if (data == null) {
			return ConfigTypes.NULL;
		}
		for (ConfigType<?> c : TYPES) {
			if (c.is(data.getClass())) {
				return c;
			}
		}
		throw new IllegalArgumentException("Type " + data.getClass().getName() + " not supported!");
	}
}
