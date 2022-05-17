package com.foxjunior.utils.idencoder

import spock.lang.Specification

class NumericCharactersMapSpec extends Specification {
	def "Chars are in map"() {
		given:
			NumericCharactersMap map = new NumericCharactersMap("9876543210".toCharArray())
			char zero = '0'
			char nine = '9'
		expect:
			map.getMappedCharacter(zero) == nine
			map.getMappedCharacter(nine) == zero
			map.getReversedCharacter(zero) == nine
			map.getReversedCharacter(nine) == zero
	}
}
