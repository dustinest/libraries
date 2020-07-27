package com.foxjunior.utils.looseversion


import spock.lang.Specification
import spock.lang.Unroll

class LooseVersionSpec extends Specification {
    @Unroll
    def "#source and #target are equal"() {
        expect:
            LooseVersion.compare(source, target) == 0
            LooseVersion.equals(source, target)
        where:
            source  | target
            "1.1.1" | "1.1.1"
            "1.1.12" | "1.1-12"
            "1.1.ab" | "1.1-ab"

    }

    @Unroll
    def "compare #source and #target == -1 and #target with #source == 1"() {
        expect:
            LooseVersion.compare(source, target) == -1
            LooseVersion.compare(target, source) == 1
        where:
            source      | target
            "1.1.1"     | "1.1.1.1"
            "1.1.1"     | "1.1.2"
            "1.1.a"     | "1.1.1"
            "1.1.1-b"   | "1.1.1-a"
            "1.1.1"     | "1.1.11"
            "1.1.11"    | "1.1.12"
            "1.1.11"    | "1.1-12"
    }
}
