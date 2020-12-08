package uk.co.alistaironeill.advent.y2020.d08

class HandheldGame(private val lines: List<CodeLine>) {

    companion object {
        fun factory(raw: String) = HandheldGame(raw.split("\n").map { CodeLine.factory(it) })

        fun fixCode(raw:String): Int {
            val original = factory(raw)
            for (i in original.lines.indices) {
                val possible = original.swapOperation(i)
                return possible.accAfterSuccessfulTermination() ?: continue
            }
            throw Exception("Couldn't find the fix!")
        }
    }

    fun accAfterLoop(): Int {
        var i = 0
        val visited = HashSet<Int>()
        var acc = 0
        while (!visited.contains(i)) {
            visited.add(i)
            val line = lines[i]
            when (line.operation) {
                Operation.nop -> { i += 1 }
                Operation.acc -> { acc += line.value; i += 1 }
                Operation.jmp -> { i += line.value }
            }
        }

        return acc
    }

    fun accAfterSuccessfulTermination(): Int? {
        var i = 0
        val visited = HashSet<Int>()
        var acc = 0
        while (!visited.contains(i)) {
            if (i == lines.size) {
                return acc
            }
            if (i > lines.size) {
                return null
            }
            visited.add(i)
            val line = lines[i]
            when (line.operation) {
                Operation.nop -> { i += 1 }
                Operation.acc -> { acc += line.value; i += 1 }
                Operation.jmp -> { i += line.value }
            }
        }

        return null
    }

    fun swapOperation(lineNo: Int): HandheldGame {
        val newLines = lines.mapIndexed{ i, line ->
            when {
                i != lineNo -> line
                line.operation == Operation.nop -> CodeLine(Operation.jmp, line.value)
                line.operation == Operation.jmp -> CodeLine(Operation.nop, line.value)
                else -> line
            }
        }
        return HandheldGame(newLines)
    }

    data class CodeLine(val operation: Operation, val value: Int) {
        companion object {
            fun factory(raw: String): CodeLine {
                val split = raw.split(" ")
                val operation = Operation.valueOf(split[0])
                val value = split[1].toInt()
                return CodeLine(operation, value)
            }
        }
    }

    enum class Operation {
        nop,
        acc,
        jmp
    }
}