package uk.co.alistaironeill.advent.y2020.d15

class MemoryGame(starting: List<Long>) {
    val mostRecent = HashMap<Long, Long>()
    var turn = 0L
    private var previous: Long = -1L

    init {
        starting.forEach {
            sayNumber(it)
        }
    }

    fun getNth(n: Long): Long {
        while (turn < n) {
            workOutNext()
        }

        return previous
    }

    private fun workOutNext() {
        val lastUsage = mostRecent[previous] ?: return sayNumber(0)
        sayNumber(turn - lastUsage)
    }

    private fun sayNumber(number: Long) {
        mostRecent[previous] = turn
        previous = number
        turn += 1
    }
}