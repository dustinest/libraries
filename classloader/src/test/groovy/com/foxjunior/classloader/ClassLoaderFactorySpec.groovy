package com.foxjunior.classloader

import spock.lang.Specification


class ClassLoaderFactorySpec extends Specification {
	def setupSpec() {
		ClassLoaderFactory.start()
	}

	def "IS annotated"() {
		expect:
			ClassLoaderFactoryTest.annotated1
			ClassLoaderFactoryTest.annotated2
			ClassLoaderFactoryTest.annotated3
			ClassLoaderFactoryTest.annotated4
	}
}
