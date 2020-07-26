package ee.fj.strings.filters

import spock.lang.Specification
import spock.lang.Unroll


class StringFiltersSpec extends Specification {
    @Unroll
    def "hasText(#text) == #result && hasNoText(#text) != #result"() {
        expect:
            StringFilters.hasText(text) == result
            StringFilters.hasNoText(text) != result
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
            StringFilters.isEmpty(text) == result
            StringFilters.isNotEmpty(text) != result
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
