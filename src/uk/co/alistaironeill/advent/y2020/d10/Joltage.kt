package uk.co.alistaironeill.advent.y2020.d10

object Joltage {
    fun task1(data: List<Int>): Int {
        var oneJumps = 0
        var threeJumps = 1
        var joltage = 0
        val sorted = data.sorted()
        for (newJoltage in sorted) {
            when (newJoltage - joltage) {
                1 -> oneJumps += 1
                2 -> {}
                3 -> threeJumps += 1
                else -> throw Exception()
            }
            joltage = newJoltage
        }

        return oneJumps * threeJumps
    }

    fun task2(input: List<Int>) = perms(listOf(0) + input.sorted())

    private fun perms(data: List<Int>): Long {
        when (data.size) {
            0, 1, 2 -> return 1L
        }

        val split = splitOn4(splitOn3(data))
        return if (split.size > 1) {
            split.map { perms(it) }.reduce{ l1, l2 -> l1*l2 }
        }
        else {
            val datum = split[0]
            val n = datum.size / 2
            val without = perms(datum.take(n) + datum.drop(n+1))
            val with = perms(datum.take(n+1)) * perms(datum.drop(n))
            with + without
        }
    }

    private fun splitOn3(data: List<Int>): List<List<Int>> {
        val ret = ArrayList<List<Int>>()
        var current = ArrayList<Int>()
        for (i in 0 until data.size - 1) {
            current.add(data[i])
            if (data[i+1] - data[i] == 3) {
                ret.add(current)
                current = ArrayList()
            }
        }

        current.add(data.last())
        ret.add(current)

        return ret
    }

    private fun splitOn4(data: List<List<Int>>): ArrayList<List<Int>> {
        val ret = ArrayList<List<Int>>()
        for (datum in data) {
            if (datum.size == 1) {
                ret.add(datum)
                continue
            }
            var current = arrayListOf(datum.first())
            for (i in 1 until datum.size - 1) {
                current.add(datum[i])
                if (datum[i+1] - datum[i-1] >= 4) {
                    ret.add(current)
                    current = arrayListOf(datum[i])
                }
            }
            current.add(datum.last())
            ret.add(current)
        }

        return ret
    }


}