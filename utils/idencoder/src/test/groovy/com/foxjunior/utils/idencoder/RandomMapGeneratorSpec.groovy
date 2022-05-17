package com.foxjunior.utils.idencoder


import spock.lang.Specification

import java.util.stream.Collectors

class RandomMapGeneratorSpec extends Specification {
	def "numbers"() {
		when:
			List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.NUMBERS)
		then:
			result.size() == 10
		when:
			String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining())
		then:
			stringResult == "0123456789"
	}

	def "lowercase"() {
		when:
			List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.LOWERCASE)
		then:
			result.size() == 26
		when:
			String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining())
		then:
			stringResult == "abcdefghijklmnopqrstuvwxyz"
	}

	def "uppercase"() {
		when:
			List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.UPPERCASE)
		then:
			result.size() == 26
		when:
			String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining())
		then:
			stringResult == "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	}

	def "lowerAndUppercase"() {
		when:
			List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.UPPERCASE, RandomMapGenerator.Properties.LOWERCASE)
		then:
			result.size() == 52
		when:
			String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining())
		then:
			stringResult == "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
	}

	def "allValues"() {
		when:
			List<Character> result = RandomMapGenerator.getChars(RandomMapGenerator.Properties.values())
			String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining())
		then:
			stringResult == "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	}

	def "mixed duplicates"() {
		when:
			List<Character> result = RandomMapGenerator.getChars(
				RandomMapGenerator.Properties.NUMBERS, RandomMapGenerator.Properties.NUMBERS,
				RandomMapGenerator.Properties.LOWERCASE,
				RandomMapGenerator.Properties.UPPERCASE,
				RandomMapGenerator.Properties.LOWERCASE
			)
			String stringResult = result.stream().map(String::valueOf).collect(Collectors.joining())
		then:
			stringResult == "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	}

	def "throw exception when empty"() {
		when:
			RandomMapGenerator.getChars()
		then:
			Exception error = thrown(IllegalArgumentException)
			error.message == "Properties must not be empty!"
	}


	def "one row random char matrix"() {
		given:
			char[] controlChars = ['r', 'n', 'h', 'o', 'y', 'q', 't', 'u', 'l', 'v'] as char[]
		when:
			char[][] chars = RandomMapGenerator.getRandomCharMatrix(1, 10, 1, RandomMapGenerator.Properties.LOWERCASE)
		then:
			chars.length == 1
			chars[0].length == 10
			for (int i = 0; i < chars[0].length; i++) {
				assert chars[0][i] == controlChars[i]
			}
	}

	def "ten rows random char matrix"() {
		given:
			char[][] controlChars = [
				['s', 'x', 'i', 'k', 'l', 't', 'g', 'n', 'j', 'q',],
				['c', 'm', 'l', 'd', 'q', 'k', 'j', 'r', 'e', 'f',],
				['w', 'x', 'z', 'n', 'p', 'a', 'd', 'l', 'm', 'f',],
				['r', 'b', 'i', 'h', 'n', 'g', 'x', 'f', 'm', 'd',],
				['k', 'f', 'q', 'y', 'o', 't', 's', 'r', 'm', 'z',],
				['x', 'q', 'k', 'e', 'w', 'p', 'y', 'n', 'j', 'h',],
				['e', 'n', 'y', 'c', 'v', 'o', 'x', 'h', 'w', 'r',],
				['f', 'm', 'd', 't', 'h', 'n', 'l', 'j', 'x', 'z',],
				['z', 's', 'm', 'e', 'q', 't', 'j', 'p', 'g', 'n',],
				['e', 'l', 'r', 'c', 'x', 'q', 'n', 'd', 'p', 'm',]
			] as char[][]
		when:
			char[][] chars = RandomMapGenerator.getRandomCharMatrix(10, 10, 2, RandomMapGenerator.Properties.LOWERCASE)
		then:
			assert chars == controlChars
	}

}
