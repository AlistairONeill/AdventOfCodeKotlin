package uk.co.alistaironeill.advent.y2020.d18

object NewMaths {
    private val additionRegex = Regex("(\\d+) \\+ (\\d+)")

    fun task1(data: String): Long {
        var sum = 0L
        data.split("\n").forEach { sum += evaluate1(it) }
        return sum
    }

    private fun evaluate1(string: String): Long {
        if (string.contains('(')) {
            val i0 = string.indexOf('(') + 1
            var i = i0
            var d = 1
            while (d > 0) {
                when (string[i]) {
                    '(' -> d += 1
                    ')' -> d -= 1
                }
                i += 1
            }

            var toEvaluate = ""
            if (i0 > 0) {
                toEvaluate += string.substring(0, i0 - 1)
            }

            toEvaluate += evaluate1(string.substring(i0, i - 1)).toString()

            if (i < string.length) {
                toEvaluate += string.substring(i)
            }

            return evaluate1(toEvaluate)
        }

        val split = string.split(" ")
        var acc = split[0].toLong()
        var i = 2
        while (i < split.size) {
            val operation = split[i-1]
            val term = split[i].toLong()
            when (operation) {
                "+" -> acc += term
                "*" -> acc *= term
                else -> throw Exception("Unknown operation!")
            }

            i += 2
        }
        return acc
    }

    fun task2(data: String): Long {
        var sum = 0L
        data.split("\n").forEach { sum += evaluate2(it) }
        return sum
    }

    fun evaluate2(string: String): Long {
        if (string.contains('(')) {
            val i0 = string.indexOf('(') + 1
            var i = i0
            var d = 1
            while (d > 0) {
                when (string[i]) {
                    '(' -> d += 1
                    ')' -> d -= 1
                }
                i += 1
            }

            var toEvaluate = ""
            if (i0 > 0) {
                toEvaluate += string.substring(0, i0 - 1)
            }

            toEvaluate += evaluate2(string.substring(i0, i - 1)).toString()

            if (i < string.length) {
                toEvaluate += string.substring(i)
            }

            return evaluate2(toEvaluate)
        }

        if (string.contains('+')) {
            val match = additionRegex.find(string)!!
            val groups = match.destructured.toList()
            val term1 = groups[0].toLong()
            val term2 = groups[1].toLong()
            val range = match.range
            val result = term1 + term2
            val i0 = range.first
            val i = range.last

            var toEvaluate = ""
            if (i0 > 0) {
                toEvaluate += string.substring(0, i0)
            }

            toEvaluate += result.toString()

            if (i < string.length - 1) {
                toEvaluate += string.substring(i + 1)
            }

            return evaluate2(toEvaluate)
        }

        val terms = string.split(" * ")
        var acc = 1L
        terms.forEach { acc *= it.toLong() }
        return acc
    }
}