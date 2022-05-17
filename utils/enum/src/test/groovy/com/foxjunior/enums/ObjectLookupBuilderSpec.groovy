package com.foxjunior.enums

import spock.lang.Specification


class ObjectLookupBuilderSpec extends Specification {
    class ObjectLookupBuilderTestVal { String value }

    def "object lookup works"() {
        given:
        ObjectLookupBuilderTestVal VALUE1 = new ObjectLookupBuilderTestVal(value: "A")
        ObjectLookupBuilderTestVal VALUE2 = new ObjectLookupBuilderTestVal(value: "B")
        def LOOKUP = ObjectLookupBuilder.build( { e ->
            return [e.getValue().toUpperCase(), e.getValue().toLowerCase()]
        }, VALUE1, VALUE2)
        when:
        def res1 = LOOKUP.apply("a")
        def res2 = LOOKUP.apply("A")
        def res3 = LOOKUP.apply("b")
        def res4 = LOOKUP.apply("B")
        then:
        res1.get() == VALUE1
        res2.get() == VALUE1
        res3.get() == VALUE2
        res4.get() == VALUE2
    }
}
