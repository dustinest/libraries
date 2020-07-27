package com.foxjunior.awt.image


import spock.lang.Specification
import spock.lang.Unroll

class ScaleCalculatorSpec extends Specification {
    @Unroll
    def "getSmallestScale #sourceW x #sourceH => #targetW x #targetH == #result"() {
        expect:
            ScaleCalculator.getSmallestScale(sourceW, sourceH, targetW, targetH) == result
        where:
            sourceW | sourceH   | targetW   | targetH   | result
            100     | 100       | 50        | 50        | 0.5
            50      | 50        | 100       | 100       | 2
            100     | 50        | 50        | 25        | 0.5
            50      | 25        | 100       | 50        | 2
            100     | 75        | 25        | 50         | 0.25
    }

    @Unroll
    def "testGetLargestScale #sourceW x #sourceH => #targetW x #targetH == #result"() {
        expect:
            ScaleCalculator.getLargestScale(sourceW, sourceH, targetW, targetH) == result
        where:
            sourceW | sourceH   | targetW   | targetH   | result
            100     | 100       | 50        | 50        | 0.5
            50      | 50        | 100       | 100       | 2
            100     | 50        | 50        | 25        | 0.5
            50      | 25        | 100       | 50        | 2
            100     | 75        | 25        | 50        | 0.6666666666666666
    }

    @Unroll
    def "targetScale #sourceW x #sourceH => #targetW x #targetH results width == #resultW and height == #resultH"() {
        expect:
            ScaleCalculator.targetScale(sourceW, sourceH, targetW, targetH, {width, height ->
                assert width == resultW
                assert height == resultH
                return null
            }) == null
        where:
            sourceW | sourceH   | targetW   | targetH   | resultW   | resultH
            100     | 100       | 50        | 50        | 50        | 50
            50      | 50        | 100       | 100       | 100       | 100
            100     | 50        | 50        | 25        | 50        | 25
            25      | 50        | 50        | 100       | 50        | 100
            100     | 75        | 25        | 50       | 25        | 18.75
    }

    @Unroll
    def "sourceScale #sourceW x #sourceH => #targetW x #targetH results width == #resultW and height == #resultH"() {
        expect:
            ScaleCalculator.sourceScale(sourceW, sourceH, targetW, targetH, {width, height ->
                assert width == resultW
                assert height == resultH
                return null
            }) == null
        where:
            sourceW | sourceH   | targetW   | targetH   | resultW   | resultH
            100     | 100       | 50        | 50        | 100       | 100
            50      | 50        | 100       | 100       | 50        | 50
            100     | 50        | 50        | 25        | 100       | 50
            25      | 50        | 50        | 100       | 25        | 50
            100     | 75        | 25        | 50        | 37.5      | 75
    }
}
