package com.foxjunior.arrays.combine


import spock.lang.Specification

class ArrayCombineSpec extends Specification {
    def "Test if last array gets combined"() {
        expect:
            ArrayCombine.combine(null, null, null, ["1","2","3"] as String[],  ["1","2","3"] as String[]) == ["1","2","3", "1","2","3"]
            ArrayCombine.combine(null, null, null, [1,2,3] as int[], [1,2,3] as int[]) == [1,2,3,1,2,3]
            ArrayCombine.combine(null, null, null, [1,2,3] as byte[], [1,2,3] as byte[]) == [1,2,3,1,2,3]
            ArrayCombine.combine(null, null, null, ['1','2','3'] as char[], ['1','2','3'] as char[]) == ['1','2','3','1','2','3']
            ArrayCombine.combine(null, null, null, [true, false] as boolean[], [false, true] as boolean[]) == [true, false, false, true]
            ArrayCombine.combine(null, null, null, [1,2,3] as long[], [1,2,3] as long[]) == [1,2,3,1,2,3]
            ArrayCombine.combine(null, null, null, [1,2,3] as float[], [1,2,3] as float[]) == [1,2,3,1,2,3]
            ArrayCombine.combine(null, null, null, [1,2,3] as double[], [1,2,3] as double[]) == [1,2,3,1,2,3]
    }

    def "Test if intermediate null values are being ignored"() {
        expect:
            ArrayCombine.combine(["1","2","3"] as String[],   null, null, ["1","2","3"] as String[]) == ["1","2","3", "1","2","3"]
            ArrayCombine.combine([1,2,3] as int[],  null, null, [1,2,3] as int[]) == [1,2,3,1,2,3]
            ArrayCombine.combine([1,2,3] as byte[],  null, null, [1,2,3] as byte[]) == [1,2,3,1,2,3]
            ArrayCombine.combine(['1','2','3'] as char[],  null, null, ['1','2','3'] as char[]) == ['1','2','3','1','2','3']
            ArrayCombine.combine([true, false] as boolean[],  null, null, [false, true] as boolean[]) == [true, false, false, true]
            ArrayCombine.combine([1,2,3] as long[],  null, null, [1,2,3] as long[]) == [1,2,3,1,2,3]
            ArrayCombine.combine([1,2,3] as float[],  null, null, [1,2,3] as float[]) == [1,2,3,1,2,3]
            ArrayCombine.combine([1,2,3] as double[],  null, null, [1,2,3] as double[]) == [1,2,3,1,2,3]
    }
}
