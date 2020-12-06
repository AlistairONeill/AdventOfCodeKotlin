package uk.co.alistaironeill.advent.y2020.d06

object Customs {
    private fun unique(string: String) = string.filter { it != '\n' }.toSet().count()
    fun sumOfUniques(strings: List<String>) = strings.sumBy { unique(it) }

    private fun all(string: String): Int {
        val lines = string.split("\n")
        return lines[0].filter { lines.all { line -> line.contains(it) } }.count()
    }

    fun sumOfAll(strings: List<String>) = strings.sumBy { all(it) }
}