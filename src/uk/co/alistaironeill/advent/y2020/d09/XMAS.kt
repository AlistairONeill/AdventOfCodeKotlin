package uk.co.alistaironeill.advent.y2020.d09

import java.lang.Exception

object XMAS {
    fun findBroken(data: List<Long>, preamble: Int): Long {
        for (i in preamble until data.size) {
            if (!isValid(data, i, preamble)) {
                return data[i]
            }
        }
        throw Exception()
    }

    fun isValid(data: List<Long>, index: Int, preamble: Int): Boolean {
        val target = data[index]
        for (i in index - preamble until index) {
            val subTarget = target - data[i]
            for (j in i + 1 until index) {
                if (data[j] == subTarget) {
                    return true
                }
            }
        }

        return false
    }

    fun findWeakness(data: List<Long>, preamble: Int): Long {
        val broken = findBroken(data, preamble)
        val numbers = findContiguousSum(data, broken)
        val min = numbers.min()!!
        val max = numbers.max()!!
        return min + max
    }

    fun findContiguousSum(data: List<Long>, target: Long): List<Long> {
        for (i in data.indices) {
            for (j in 1 until data.size - i) {
                val test = data.drop(i).take(j)
                val sum = test.sum()
                if (sum > target) break
                if (sum == target) return test
            }
        }
        throw Exception()
    }
}