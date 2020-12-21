package uk.co.alistaironeill.advent.y2020.d20

import uk.co.alistaironeill.advent.y2020.extensions.maxOf
import uk.co.alistaironeill.advent.y2020.extensions.minOf
import uk.co.alistaironeill.advent.y2020.extensions.sqrt

class JurassicJigsaw(raw: String) {
    val tiles = raw.split("\n\n").map { Tile(it) }

    init {
        for (i in tiles.indices) {
            val from = tiles[i]
            for (j in i+1 until tiles.size) {
                val to = tiles[j]
                for (x in from.states.indices) {
                    val fromState = from.states[x]
                    for (y in to.states.indices) {
                        val toState = to.states[y]
                        if (fromState.right == toState.left) {
                            fromState.rightLinks.add(to to y)
                            toState.leftLinks.add(from to x)
                        }

                        if (fromState.bottom == toState.top) {
                            fromState.downLinks.add(to to y)
                            toState.upLinks.add(from to x)
                        }

                        if (fromState.left == toState.right) {
                            fromState.leftLinks.add(to to y)
                            toState.rightLinks.add(from to x)
                        }

                        if (fromState.top == toState.bottom) {
                            fromState.upLinks.add(to to y)
                            toState.downLinks.add(from to x)
                        }
                    }
                }
            }
        }
    }

    fun testStats() {
        val minConnections = tiles.minOf { it.states.maxOf { it.connections } }
        println("Min connections is $minConnections")

        val filtered = tiles.filter { it.states.maxOf { it.connections} == minConnections }
        println("With only $minConnections connections: ${filtered.count()}")

        val counts = filtered.map { it.states.count { it.connections == minConnections } }
        println(counts)
    }

    fun task1(): Long {
        var acc = 1L
        tiles.filter { it.states.maxOf { it.connections } == 2 }.forEach { acc *= it.id }
        return acc
    }

    fun task2(): Int {
        val solver = Solver(tiles)
        val image = solver.solve()

        val roughSeas = listOf(
            image,
            image.rotate,
            image.rotate.rotate,
            image.rotate.rotate.rotate,
            image.reflect,
            image.reflect.rotate,
            image.reflect.rotate.rotate,
            image.reflect.rotate.rotate.rotate
        ).map { it.getRoughSeas() }

        println(roughSeas)

        return roughSeas.min()!!
    }

    fun solve() {
        val solver = Solver(tiles)
        solver.solve()
    }

    class Solver(tiles: List<Tile>) {
        private val tiles = ArrayList<Tile>().apply { tiles.forEach { add(it) } }
        private val size = tiles.size.sqrt
        private val solution = Array<Array<Pair<Tile, Int>?>>(size) { Array(size) { null } }

        init {
            val topLeft = tiles.first { it.states.maxOf { it.connections } == 2 }
            val stateIndex = topLeft.states.indices.first { topLeft.states[it].downLinks.size > 0 && topLeft.states[it].rightLinks.size > 0}
            place(
                topLeft,
                stateIndex, //Can do this WLOG
                0,
                0
                )
        }

        private fun place(tile: Tile, stateIndex: Int, x: Int, y: Int) {
            solution[y][x] = tile to stateIndex
            tiles.forEach { it ->
                it.states.forEach { state ->
                    state.rightLinks.removeIf { it.first == tile}
                    state.leftLinks.removeIf { it.first == tile}
                    state.upLinks.removeIf { it.first == tile}
                    state.downLinks.removeIf { it.first == tile}
                }
            }
            tiles.remove(tile)
        }

        fun solve(): Image {
            while (tiles.isNotEmpty()) {
                val success = findNext()
                if (!success) {
                    throw Exception("Can't solve this! :'(")
                }
            }

            return Image.factory(solution)
        }

        private fun findNext(): Boolean {
            for (y in solution.indices) {
                for (x in solution[y].indices) {
                    if (solution[y][x] != null) { continue }

                    if (y > 0) {
                        val above = solution[y-1][x]
                        if (above != null) {
                            val aboveState = above.first.states[above.second]
                            if (aboveState.downLinks.size == 1) {
                                val downLink = aboveState.downLinks.first()
                                place(downLink.first, downLink.second, x, y)
                                return true
                            }
                        }
                    }

                    if (x > 0) {
                        val left = solution[y][x-1]
                        if (left != null) {
                            val leftState = left.first.states[left.second]
                            if (leftState.rightLinks.size == 1) {
                                val rightLink = leftState.rightLinks.first()
                                place(rightLink.first, rightLink.second, x, y)
                                return true
                            }
                        }
                    }
                }
            }

            return false
        }
    }

    class Tile(raw: String) {
        companion object {
            val idRegex = Regex("^Tile (\\d+):$")
        }

        val id: Int

        val states: List<State>

        init {
            val split = raw.split("\n")
            id = idRegex.find(split[0])!!.groupValues[1].toInt()
            val data = split.drop(1).map {
                it.map {
                    when (it) {
                        '.' -> "0"
                        '#' -> "1"
                        else -> throw Exception()
                    }
                }
            }

            val top = data[0].joinToString("").toInt(2)
            val right = data.joinToString(""){it.last()}.toInt(2)
            val bottom = data.last().joinToString("").toInt(2)
            val left = data.joinToString(""){it.first()}.toInt(2)
            val imageData = data.drop(1).dropLast(1).map {
                it.drop(1).dropLast(1).map {
                    when (it) {
                        "0" -> false
                        "1" -> true
                        else -> throw Exception()
                    }
                }.toTypedArray()
            }.toTypedArray()
            val state = State(top, right, bottom, left, imageData)
            states = listOf(state,
                state.rotate,
                state.rotate.rotate,
                state.rotate.rotate.rotate,
                state.reflect,
                state.reflect.rotate,
                state.reflect.rotate.rotate,
                state.reflect.rotate.rotate.rotate).distinct()
        }

        class State(
            val top: Int,
            val right: Int,
            val bottom: Int,
            val left: Int,
            val imageData: Array<Array<Boolean>>
        ) {
            companion object {
                fun invert(input: Int) = input.toString(2).padStart(10, '0').reversed().toInt(2)
            }

            val rightLinks = ArrayList<Pair<Tile, Int>>()
            val downLinks = ArrayList<Pair<Tile, Int>>()
            val leftLinks = ArrayList<Pair<Tile, Int>>()
            val upLinks = ArrayList<Pair<Tile, Int>>()

            val dataSize = imageData.size

            val connections get() = listOf(rightLinks, downLinks, leftLinks, upLinks).count { it.isNotEmpty() }

            val rotate get(): State {
                val newImageData = Array(dataSize) { Array(dataSize) { false } }

                for (y in imageData.indices) {
                    for (x in imageData[y].indices) {
                        newImageData[x][dataSize - 1 - y] = imageData[y][x]
                    }
                }

                return State(
                    invert(left),
                    top,
                    invert(right),
                    bottom,
                    newImageData
                )
            }

            val reflect get(): State {
                val newImageData = Array(dataSize) { Array(dataSize) { false } }

                for (y in imageData.indices) {
                    for (x in imageData[y].indices) {
                        newImageData[y][dataSize - 1 - x] = imageData[y][x]
                    }
                }

                return State(
                    invert(top),
                    left,
                    invert(bottom),
                    right,
                    newImageData
                )
            }
        }
    }

    class Image(val data: Array<Array<Boolean>>) {
        companion object {
            fun factory(solution: Array<Array<Pair<Tile, Int>?>>): Image {
                val data = ArrayList<ArrayList<Boolean>>()
                solution.forEach { row ->
                    val states = row.map { it!!.first.states[it.second] }
                    for (y in 0 until states[0].dataSize) {
                        val rowData = ArrayList<Boolean>()
                        states.forEach {
                            it.imageData[y].forEach {
                                rowData.add(it)
                            }
                        }
                        data.add(rowData)
                    }
                }

                return Image(data.map { it.toTypedArray() }.toTypedArray())
            }
        }

        private val dataSize = data.size

        val rotate: Image get() {
            val newImageData = Array(dataSize) { Array(dataSize) { false } }

            for (y in data.indices) {
                for (x in data[y].indices) {
                    newImageData[x][dataSize - 1 - y] = data[y][x]
                }
            }

            return Image(newImageData)
        }

        val reflect: Image get() {
            val newImageData = Array(dataSize) { Array(dataSize) { false } }

            for (y in data.indices) {
                for (x in data[y].indices) {
                    newImageData[y][dataSize - 1 - x] = data[y][x]
                }
            }

            return Image(newImageData)
        }

        fun getRoughSeas(): Int {
            var total = data.sumBy { it.count { it } }

            for (x in 0 until dataSize - 19) {
                for (y in 0 until dataSize - 2) {
                    if (isMonsterAt(x, y)) {
                        total -= 15
                    }
                }
            }

            return total
        }

        fun isMonsterAt(x: Int, y:Int) =
            data[y+1][x]
            && data[y+2][x+1]
            && data[y+2][x+4]
            && data[y+1][x+5]
            && data[y+1][x+6]
            && data[y+2][x+7]
            && data[y+2][x+10]
            && data[y+1][x+11]
            && data[y+1][x+12]
            && data[y+2][x+13]
            && data[y+2][x+16]
            && data[y+1][x+17]
            && data[y][x+18]
            && data[y+1][x+18]
            && data[y+1][x+19]
    }
}