package uk.co.alistaironeill.advent.y2020.d22

import kotlin.math.max

class RecursiveCrabCombat(deck1: List<Int>, deck2: List<Int>) {
    companion object {
        fun factory(data: String) : RecursiveCrabCombat {
            val split = data.split("\n\n")

            val player1 = Deck.factory(split[0])
            val player2 = Deck.factory(split[1])

            return RecursiveCrabCombat(player1.cards, player2.cards)
        }
    }

    val player1 = Deck(deck1)
    val player2 = Deck(deck2)

    val winnersScore get() = max(player1.getScore(), player2.getScore())

    val history = ArrayList<Pair<List<Int>, List<Int>>>()

    fun play(): Boolean {
        var turn = takeTurn()
        while (turn == null) {
            turn = takeTurn()
        }
        return turn
    }

    fun takeTurn(): Boolean? {
        if (player1.cards.isEmpty()) {
            return false
        }

        if (player2.cards.isEmpty()) {
            return true
        }

        val newHistory = Pair(player1.cards.toList(), player2.cards.toList())
        if (history.contains(newHistory)) {
            return true
        }
        history.add(newHistory)

        val card1 = player1.draw()
        val card2 = player2.draw()

        if (card1 > player1.size || card2 > player2.size) {
            when {
                card1 > card2 -> player1.add(card1, card2)
                card2 > card1 ->  player2.add(card2, card1)
                else -> throw Exception()
            }
            return null
        }

        val deck1 = player1.cards.take(card1)
        val deck2 = player2.cards.take(card2)

        val subGame = RecursiveCrabCombat(deck1, deck2)
        val result = subGame.play()
        if (result) {
            player1.add(card1, card2)
        }
        else {
            player2.add(card2, card1)
        }
        return null
    }

    class Deck(data: List<Int>) {
        companion object {
            fun factory(data: String): Deck {
                val cards = ArrayList<Int>().apply {
                    data.split("\n").drop(1).map { it.toInt() }.forEach {
                        add(it)
                    }
                }
                return Deck(cards);
            }
        }

        val cards = ArrayList<Int>().apply { addAll(data) }
        val size get() = cards.size

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