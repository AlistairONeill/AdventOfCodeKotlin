package uk.co.alistaironeill.advent.y2020.d14

import java.lang.Exception

class DockingData2 {
    companion object {
        val assignmentRegex = Regex("^mem\\[(\\d+)\\]$")
    }

    private val mask = BitMask()
    private val memory = HashMap<Long, Long>()

    fun process(raw: String): Long {
        raw.split("\n").forEach {
            processLine(it)
        }

        return memory.values.sum()
    }

    private fun processLine(raw: String) {
        val split = raw.split(" = ")
        if (split[0] == "mask") {
            mask.updateMask(split[1])
        }
        else {
            updateMemory(split[0], split[1].toLong())
        }
    }


    private fun updateMemory(assign: String, value: Long) {
        val register = assignmentRegex.find(assign)!!.groupValues[1].toLong()
        mask.applied(register).forEach {
            memory[it] = value
        }
    }

    class BitMask {
        lateinit var masks: ArrayList<String>

        fun updateMask(mask: String) {
            masks = ArrayList()
            addMask(mask)
        }

        private fun addMask(mask: String) {
            if (mask.contains('X')) {
                addMask(mask.replaceFirst('X', 'A'))
                addMask(mask.replaceFirst('X', 'B'))
            }
            else {
                masks.add(mask)
            }
        }

        fun applied(input: Long): List<Long> {
            val binary = input.toString(2).padStart(36, '0')
            return masks.map { mask ->
                (0 until 36).map {
                    when (mask[it]) {
                        '1' -> '1'
                        '0' -> binary[it]
                        'A' -> '1'
                        'B' -> '0'
                        else -> throw Exception()
                    }
                }.joinToString("").toLong(2)
            }
        }
    }
}