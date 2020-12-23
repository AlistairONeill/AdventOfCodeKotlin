package uk.co.alistaironeill.advent.y2020.d23

import kotlin.math.max
import kotlin.math.min

class CrabCups(data: String) {
    var labelToCup: Array<Cup>
    var current: Cup
    var highest: Int
    val lowest: Int

    init {
        val split = data.map { it.toString().toInt() }
        val labelToCup: Array<Cup?> = Array(split.size + 1) { null }
        labelToCup[0] = Cup(Integer.MIN_VALUE)
        current = Cup(split[0])
        labelToCup[current.label] = current
        var highest = current.label
        var lowest = current.label
        var prev = current
        split.drop(1).forEach {
            val new = Cup(it)
            labelToCup[new.label] = new
            highest = max(highest, new.label)
            lowest = min(lowest, new.label)

            prev.next = new
            prev = new
        }
        prev.next = current
        @Suppress("UNCHECKED_CAST")
        this.labelToCup = labelToCup as Array<Cup>
        this.highest = highest
        this.lowest = lowest
    }

    fun task1(): String {
        repeat(100) { takeTurn() }
        var foo = labelToCup[1]
        foo = foo.next
        val list = ArrayList<Int>()
        while (foo.label != 1) {
            list.add(foo.label)
            foo = foo.next
        }

        return list.joinToString("")
    }

    fun task2(): Long {
        addInExtraCups()
        repeat(10000000) { takeTurn() }

        var foo = current
        while (foo.label != 1) {
            foo = foo.next
        }

        foo = foo.next
        return foo.label.toLong() * foo.next.label.toLong()
    }

    private fun addInExtraCups() {
        val labelToCup: Array<Cup?> = Array(1000001){ null }

        this.labelToCup.forEachIndexed { index, cup ->
            labelToCup[index] = cup
        }

        var prev = current.next
        while (prev.next != current) {
            prev = prev.next
        }

        (highest + 1 .. 1000000).forEach {
            val new = Cup(it)
            labelToCup[new.label] = new
            prev.next = new
            prev = new
        }

        prev.next = current
        highest = 1000000

        @Suppress("UNCHECKED_CAST")
        this.labelToCup = labelToCup as Array<Cup>
    }

    private fun takeTurn() {
        /*println("Current cups:")
        var foo = current.next
        print(current.label)
        print(", ")
        while (foo != current) {
            print(foo.label)
            print(", ")
            foo = foo.next
        }
        println("")*/

        val next = current.next

        val bannedLabels = listOf(
            next,
            next.next,
            next.next.next
        ).map { it.label }

        current.next = current.next.next.next.next



        var label = current.label - 1
        if (label < lowest) {
            label = highest
        }
        while (bannedLabels.contains(label)) {
            label -= 1
            if (label < lowest) {
                label = highest
            }
        }
        val destination = labelToCup[label]

        val afterDestination = destination.next
        destination.next = next
        next.next.next.next = afterDestination

        current = current.next
    }

    class Cup(val label: Int) {
        lateinit var next: Cup
    }
}