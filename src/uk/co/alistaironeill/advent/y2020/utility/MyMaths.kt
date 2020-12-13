package uk.co.alistaironeill.advent.y2020.utility

import java.math.BigInteger

object MyMaths {
    fun extendedEuclidean(a: BigInteger, b: BigInteger, x: MutableBigInteger, y: MutableBigInteger): BigInteger {
        if (a == BigInteger.ZERO) {
            x.value = BigInteger.ZERO
            y.value = BigInteger.ONE
            return b
        }

        if (a > b) {
            return extendedEuclidean(b, a, y, x)
        }

        val x1 = MutableBigInteger()
        val y1 = MutableBigInteger()
        val gcd = extendedEuclidean(b % a, a, x1, y1)

        x.value = y1.value - (b/a) * x1.value
        y.value = x1.value

        return gcd
    }
}