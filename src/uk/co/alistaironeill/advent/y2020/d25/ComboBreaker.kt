package uk.co.alistaironeill.advent.y2020.d25

object ComboBreaker {
    const val MOD = 20201227L


    fun crack(subject: Long, key: Long): Long {
        var i = 0L
        var number = 1L
        while (number != key) {
            number *= subject
            number %= MOD
            i += 1L
        }
        return i
    }

    fun transform(subject: Long, loopSize: Long): Long {
        var number = 1L
        var i = 0L
        while (i < loopSize) {
            number *= subject
            number %= MOD
            i += 1
        }
        return number
    }

    fun encryptionKey(doorKey: Long, cardKey: Long): Long {
        val doorLoopSize = crack(7, doorKey)
        return transform(cardKey, doorLoopSize)
    }

    fun task1(data: String): Long {
        val split = data.split("\n")
        return encryptionKey(split[0].toLong(), split[1].toLong())
    }
}