package uk.co.alistaironeill.advent.y2020.utility

import java.math.BigInteger

class MutableBigInteger {
    var value: BigInteger = BigInteger.ZERO
    operator fun times(other: BigInteger) = value * other
    override fun toString() = value.toString()
}

operator fun Long.times(other: MutableBigInteger) = other.value * this.toBigInteger()
operator fun Int.times(other: MutableBigInteger) = other.value * this.toBigInteger()
