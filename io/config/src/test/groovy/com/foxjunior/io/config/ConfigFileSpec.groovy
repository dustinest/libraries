package com.foxjunior.io.config


import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicInteger

class ConfigFileSpec extends Specification {
	private static final Object[] LINE1 = new Object[] { "Lorem", true, false, 1L, 2.5d, 3.5f, 4, null, Calendar.getInstance().getTime(), LocalDateTime.now(), ZonedDateTime.now(), LocalDate.now()}
	private static final Object[] LINE2 = new Object[] { "Ipsum", false, true, 2L, 3.5d, 4.5f, 5, Calendar.getInstance().getTime(), null}
	private static final byte[] DATA = new byte[] {0,0,0,1,1,0,0,0,5,108,111,114,101,109,1,0,0,0,5,105,112,115,117,109,3,0,0,0,4,0,0,0,1,3,0,0,0,4,0,0,0,2,4,0,0,0,4,65,112,0,0,0}

	def "Writing works"() {
		given:
			ByteArrayOutputStream out = new ByteArrayOutputStream()
		when:
			try (ConfigFileWriter writer = new ConfigFileWriter(1, out)) {
				writer.write("lorem", "ipsum", 1, 2, 15f, null)
			}
		then:
			out.toByteArray() == DATA
	}

	def "Reading works"() {
		given:
			ConfigFileReader reader = new ConfigFileReader(new ByteArrayInputStream(DATA))
		expect:
			reader.getVersion() == 1
			reader.readNext(String.class) == "lorem"
			reader.readNext() == "ipsum"
			reader.readNext(Integer.class) == 1
			reader.readNext() == 2
			reader.readNext(Float.class) == 15f
			reader.readNext() == null
		cleanup:
			reader.close()
	}

	def "When reading one line too long"() {
		given:
			ConfigFileReader reader = new ConfigFileReader(new ByteArrayInputStream(DATA))
		when:
			reader.getVersion() == 1
			reader.readNext(String.class) == "lorem"
			reader.readNext() == "ipsum"
			reader.readNext(Integer.class) == 1
			reader.readNext() == 2
			reader.readNext(Float.class) == 15f
			reader.readNext() == null
			reader.readNext()
		then:
			Exception ex = thrown(EOFException)
			ex.message == "File ended!"
		cleanup:
			reader.close()
	}

	def "iterator works"() {
		given:
			final int version = 25
			final ByteArrayOutputStream out = new ByteArrayOutputStream()
			try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
				writer.writeValues(LINE1)
			}
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray())
			ConfigFileReader reader = new ConfigFileReader(inputStream)
			final AtomicInteger index = new AtomicInteger(0)
		when:
			for (Object o : reader) {
				assert LINE1[index.getAndIncrement()] == o
			}
		then:
			index.get() == LINE1.length
		cleanup:
			reader.close()
	}

	def "test io"() {
		given:
			final int version = 25
			ByteArrayOutputStream out = new ByteArrayOutputStream()
			try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
				for (Object o : LINE1) {
					writer.write(o)
				}
				writer.writeValues(LINE2)
			}
			ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray())
			ConfigFileReader reader = new ConfigFileReader(inputStream)
		expect:
			reader.getVersion() == version
		when:
			def result = reader.read{type, data ->
				assert type == ConfigTypes.STRING
				assert type.getValue(data) == LINE1[0]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.BOOLEAN
				final ConfigType<Boolean> parser = type.as(Boolean.class)
				assert parser.getValue(data)
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.BOOLEAN
				assert type.getValue(data) == LINE1[2]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.LONG
				assert type.getValue(data) == LINE1[3]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.DOUBLE
				assert type.getValue(data) == LINE1[4]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.FLOAT
				assert type.getValue(data) == LINE1[5]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.INTEGER
				assert type.getValue(data) == LINE1[6]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.NULL
				assert type.getValue(data) == null
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.DATE
				assert type.getValue(data) == LINE1[8]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.LOCALDATETIME
				assert type.getValue(data) == LINE1[9]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.ZONEDDATETIME
				assert type.getValue(data) == LINE1[10]
				return false
			}
		then:
			result == 1
		when:
			result = reader.read{type, data ->
				assert type == ConfigTypes.LOCALDATE
				assert type.getValue(data) == LINE1[11]
				return false
			}
		then:
			result == 1
		expect:
			for (Object o : LINE2) {
				assert reader.readNext() == o
			}
		when:
			reader.readNext()
		then:
			Exception error = thrown(IOException)
			error.message == "File ended!"
	}

	def "Custom writing works"() {
		given:
			final int version = 25
			final Object[] dataToWrite = new Object[100000]
			final ByteArrayOutputStream out = new ByteArrayOutputStream()
			try (ConfigFileWriter writer = new ConfigFileWriter(version, out)) {
				Random rnd = new Random()
				for (int i = 0; i < dataToWrite.length; i++) {
					dataToWrite[i] = LINE1[rnd.nextInt(LINE1.length)]
					writer.write(dataToWrite[i])
				}
			}
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray())
		expect:
			try (ConfigFileReader reader = new ConfigFileReader(inputStream)) {
				AtomicInteger index = new AtomicInteger(0)
				long result = reader.read((type, data) -> {
					assert dataToWrite[index.get()] == type.getValue(data)
					index.addAndGet(1)
					return true
				})
				assert dataToWrite.length == result
			}
	}
}
