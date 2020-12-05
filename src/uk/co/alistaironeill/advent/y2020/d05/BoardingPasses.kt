package uk.co.alistaironeill.advent.y2020.d05

object BoardingPasses {
    fun codeToId(code: String): Int {
        var id = 0
        code.forEach {
            id *= 2
            id += when (it) {
                'B', 'R' -> 1
                'F', 'L' -> 0
                else -> throw Exception()
            }
        }
        return id
    }

    fun missingSeat(codes: List<String>): Int {
        val seats = codes.map { codeToId(it) }.sorted()
        var i = 0
        var j = seats[0]
        while (j == seats[i]) {
            i += 1
            j += 1
        }
        return j
    }

    fun maxId(codes: List<String>): Int {
        val max = codes.maxBy { codeToId(it) }!!
        return codeToId(max)
    }
}