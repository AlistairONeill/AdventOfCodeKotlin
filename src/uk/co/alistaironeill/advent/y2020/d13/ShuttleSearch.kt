package uk.co.alistaironeill.advent.y2020.d13

import uk.co.alistaironeill.advent.y2020.utility.MutableBigInteger
import uk.co.alistaironeill.advent.y2020.utility.MyMaths
import java.math.BigInteger

object ShuttleSearch {
    fun task1(data: String): Long {
        val split = data.split("\n")
        val start = split[0].toLong()
        var time = start
        val busIds = split[1].split(",").filter { it != "x" }.map { it.toLong() }

        while (true) {
            for (busId in busIds) {
                if (time % busId == 0L) {
                    return (time - start) * busId
                }
            }

            time += 1
        }
    }

    fun task2(raw: String) = solve(parseData(raw))

    private fun solve(data: List<Pair<BigInteger, BigInteger>>): BigInteger {
        if (data.size == 1) {
            return data[0].first
        }

        val x = MutableBigInteger()
        val y = MutableBigInteger()

        MyMaths.extendedEuclidean(data[0].second, data[1].second, x, y)

        val t = x * data[1].first * data[0].second + y * data[0].first * data[1].second
        val m = data[0].second * data[1].second

        return solve(listOf(t.mod(m) to m) + data.drop(2))
    }

    private fun parseData(raw: String): List<Pair<BigInteger, BigInteger>> {
        val data = raw.split("\n")[1].split(",")
        val busIds = ArrayList<Pair<BigInteger, BigInteger>>()
        for (i in data.indices) {
            val busId = data[i]
            if (busId == "x") {
                continue
            }
            busIds.add(-i.toBigInteger() to busId.toBigInteger())
        }
        return busIds
    }
}