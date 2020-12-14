package uk.co.alistaironeill.advent.y2020.d14

import java.lang.Exception


class DockingData1 {
    companion object {
        val assignmentRegex = Regex("^mem\\[(\\d+)\\]$")
    }

    private val mask = BitMask()
    private val memory = HashMap<Int, Long>()

    fun process(raw: String): Long {
        raw.split("\n").forEach {
            processLine(it)
        }

        return memory.values.sum()
    }

    private fun processLine(raw: String) {
        val split = raw.split(" = ")
        if (split[0] == "mask") {
            mask.mask = split[1]
        }
        else {
            updateMemory(split[0], split[1].toLong())
        }
    }


    private fun updateMemory(assign: String, value: Long) {
        val register = assignmentRegex.find(assign)!!.groupValues[1].toInt()
        memory[register] = mask.applied(value)
    }

    class BitMask {
        lateinit var mask: String

        fun applied(input: Long): Long {
            val binary = input.toString(2).padStart(36, '0')
            return (0 until 36).map {
                when (mask[it]) {
                    'X' -> binary[it]
                    '1' -> '1'
                    '0' -> '0'
                    else -> throw Exception()
                }
            }.joinToString("").toLong(2)
        }
    }
}