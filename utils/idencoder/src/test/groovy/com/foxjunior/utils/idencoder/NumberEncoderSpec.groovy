package com.foxjunior.utils.idencoder


import spock.lang.Specification

class NumberEncoderSpec extends Specification {
	private static final char[] NUMBER_TO_CHAR_ARRAY =  "0123456789".toCharArray()
	private static final char[][] CHAR_MATRIX = new char[10][]
	static {
		for (int row = 0; row < 10; row++) {
			CHAR_MATRIX[row] = NUMBER_TO_CHAR_ARRAY
		}
	}

	def "primitive fill length test"() {
		given:
			NumberEncoder numberEncoder = new NumberEncoder(true, CHAR_MATRIX, NUMBER_TO_CHAR_ARRAY)
			char[] result = new char[6]
		when:
			int position = numberEncoder.fillLength(result, 0, 4, 2)
		then:
			position == 2
		when:
			position = numberEncoder.fillLength(result, position, 123, 3)
		then:
			position == 6
		and:
			String.valueOf(result) == "143123"
	}

	def "primitive encode test"() {
		given:
			NumberEncoder numberEncoder = new NumberEncoder(true, CHAR_MATRIX, NUMBER_TO_CHAR_ARRAY)
		when:
			String result = numberEncoder.encode(2,4,8)
		then:
			result == "211241148118"
		when:
			List<Long> numbers = numberEncoder.decode(result)
		then:
			numbers == [2L, 4L, 8L]
		when:
			result = numberEncoder.encode(Long.MAX_VALUE, 15)
		then:
			result == "9219922337203685477580711215"
		when:
			numbers = numberEncoder.decode(result)
		then:
			numbers == [Long.MAX_VALUE, 15]
	}

	def "encode test"() {
		given:
			NumberEncoder numberEncoder = new NumberEncoder(1, RandomMapGenerator.Properties.LOWERCASE)
		when:
			String result = numberEncoder.encode(2,4,8)
		then:
			result == "hiiyykkslnns"
		when:
			List<Long> numbers = numberEncoder.decode(result)
		then:
			numbers == [2L ,4L, 8L]
		when:
			result = numberEncoder.encode(Long.MAX_VALUE, 15)
		then:
			result == "vqbddqqoonqjocmgvnngmjnnynyu"
		when:
			numbers = numberEncoder.decode(result)
		then:
			numbers == [Long.MAX_VALUE, 15L]
	}

	def "fill length test"() {
		given:
			NumberEncoder numberEncoder = new NumberEncoder(1, RandomMapGenerator.Properties.LOWERCASE)
			char[] result = new char[6]
		when:
			int position = numberEncoder.fillLength(result, 0, 4, 2)
		then:
			position == 2
		when:
			position = numberEncoder.fillLength(result, position, 123, 3)
		then:
			position == 6
		and:
			String.valueOf(result) == "ixdead"
	}

	def "fill char array"() {
		given:
			NumberEncoder numberEncoder = new NumberEncoder(1, RandomMapGenerator.Properties.LOWERCASE)
			char[] result = new char[28]
		when:
			int index = numberEncoder.fillCharArray("1234567890", result, 0)
			index = numberEncoder.fillCharArray("0987654321", result, index)
		then:
			index == 28
			String.valueOf(result) == "nnyryndcuptivrrhnrrvlutqyohn"
	}
}
