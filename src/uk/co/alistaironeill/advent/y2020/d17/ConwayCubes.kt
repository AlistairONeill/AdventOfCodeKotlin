package uk.co.alistaironeill.advent.y2020.d17

class ConwayCubes(raw: String, maxTurns: Int, isHyper: Boolean) {
    private val cubes: List<Cube>

    init {
        val data = raw.split("\n").map { it.toList() }
        val height = data.size + maxTurns * 2
        val width = data.size + maxTurns * 2
        val zHeight = 1 + maxTurns*2

        val wHeight = if (isHyper) 1 + maxTurns*2 else 1

        val array = Array(wHeight) { Array(zHeight){ Array(height) { Array(width) { Cube(false) } } } }

        for (y in data.indices) {
            for (x in data[y].indices) {
                val startingState = when (data[y][x]) {
                    '.' -> false
                    '#' -> true
                    else -> throw Exception()
                }

                array[if (isHyper) maxTurns else 0][maxTurns][y + maxTurns][x + maxTurns] = Cube(startingState)
            }
        }

        val allCubes = ArrayList<Cube>()
        for (w in array.indices) {
            for (z in array[w].indices) {
                for (y in array[w][z].indices) {
                    for (x in array[w][z][y].indices) {
                        for (dW in (-1..1)) {
                            for (dZ in (-1..1)) {
                                for (dY in (-1..1)) {
                                    for (dX in (-1..1)) {
                                        if (dX == 0 && dY == 0 && dZ == 0 && dW == 0) continue
                                        try {
                                            val cube = array[w + dW][z + dZ][y + dY][x + dX]
                                            array[w][z][y][x].addLink(cube)
                                        } catch (e: Exception) {
                                            //meh
                                        }
                                    }
                                }
                            }
                        }

                        allCubes.add(array[w][z][y][x])
                    }
                }
            }
        }

        cubes = allCubes.toList()
    }

    fun iterate(n: Int) {
        repeat(n) {
            iterate()
        }
    }

    fun iterate() {
        cubes.forEach {
            it.findNewState()
        }

        cubes.forEach {
            it.lockIn()
        }
    }

    fun count() = cubes.count { it.state }


    class Cube(var state: Boolean) {
        private var newState = false
        val adjacent = ArrayList<Cube>()

        fun lockIn() {
            state = newState
        }

        fun addLink(cube: Cube) = adjacent.add(cube)

        fun findNewState() {
            val count = adjacent.count { it.state }
            newState = if (state) {
                (count == 2 || count == 3)
            } else {
                count == 3
            }
        }
    }
}