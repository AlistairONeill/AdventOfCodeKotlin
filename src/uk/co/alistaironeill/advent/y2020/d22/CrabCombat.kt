package uk.co.alistaironeill.advent.y2020.d22

class CrabCombat(data: String) {
    val player1: Deck
    val player2: Deck

    init {
        val split = data.split("\n\n")
        player1 = Deck(split[0])
        player2 = Deck(split[1])
    }

    private fun play() {
        while (player1.isAlive && player2.isAlive) {
            takeTurn()
        }
    }

    private fun takeTurn() {
        val card1 = player1.draw()
        val card2 = player2.draw()

        when {
            card1 > card2 -> player1.add(card1, card2)
            card2 > card1 -> player2.add(card2, card1)
            else -> throw Exception()
        }
    }

    private fun winnersScore(): Long {
        return when {
            player1.isAlive -> player1.getScore()
            player2.isAlive -> player2.getScore()
            else -> throw Exception()
        }
    }

    fun task1(): Long {
        play()
        return winnersScore()
    }

    class Deck(data: String) {
        private val cards: ArrayList<Int>
        val isAlive get() = cards.isNotEmpty()

        init {
            cards = ArrayList<Int>().apply {
                data.split("\n").drop(1).map { it.toInt() }.forEach {
                    add(it)
                }
            }
        }

        fun draw() = cards.removeAt(0)

        fun add(winner: Int, loser: Int) {
            cards.add(winner)
            cards.add(loser)
        }

        fun getScore(): Long {
            var acc = 0L
            val size = cards.size
            for (i in 1 .. size) {
                acc += cards[size - i] * i
            }
            return acc
        }
    }
}