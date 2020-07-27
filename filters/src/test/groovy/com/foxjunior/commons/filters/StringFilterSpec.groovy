package com.foxjunior.commons.filters


import spock.lang.Specification
import spock.lang.Unroll


class StringFilterSpec extends Specification {
    @Unroll
    def "hasText(#text) == #result && hasNoText(#text) != #result"() {
        expect:
            StringFilter.hasText(text) == result
            StringFilter.hasNoText(text) != result
        where:
            text			| result
            null            | false
            "test"			| true
            " t e	s t "   | true
            ""				| false
            "	 "			| false
            "	👍 "		| true
            "	\t\n\r "    | false
    }


    @Unroll
    def "isEmpty(#text) == #result && isNotEmpty(#text) != #result"() {
        expect:
            StringFilter.isEmpty(text) == result
            StringFilter.isNotEmpty(text) != result
        where:
            text			| result
            null            | true
            "test"			| false
            " t e	s t "   | false
            ""				| true
            "	 "			| false
            "	👍 "		| false
            "	\t\n\r "    | false
    }

}
