package uk.co.alistaironeill.advent.y2020.d11

class Seating(data: String, seatLinker: (x:Int, y:Int, Seating:Seating) -> List<Pair<Int, Int>>?) {
    companion object {
        private val directions = listOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to -1,
            0 to 1,
            1 to -1,
            1 to 0,
            1 to 1
        )

        val task1Linker = { x: Int, y: Int, seating: Seating ->
            when (seating.isSeat(x, y)) {
                true -> directions.map{ it.first + x to it.second + y }.filter{ seating.isSeat(it.first, it.second) == true }
                else -> null
            }
        }

        val task2Linker = { x: Int, y: Int, seating: Seating ->
            when (seating.isSeat(x, y)) {
                true -> {
                    fun getInDirection(d: Pair<Int, Int>): Pair<Int, Int>? {
                        var tX = x
                        var tY = y

                        while (true) {
                            tX += d.first
                            tY += d.second

                            when (seating.isSeat(tX, tY)) {
                                true -> return tX to tY
                                null -> return null
                            }
                        }
                    }

                    directions.mapNotNull { getInDirection(it) }
                }
                else -> null
            }
        }
    }
    private val seats: Array<Array<Boolean?>> = data.split("\n").map {
        it.map {
            when (it) {
                '.' -> null
                'L' -> false
                else -> throw Exception()
            }
        }.toTypedArray()
    }.toTypedArray()

    private val height: Int = seats.size
    private val width: Int = seats[0].size
    private val willChange: Array<Array<Boolean>> = Array(height) { Array(width) { false } }
    private val links: Array<Array<List<Pair<Int, Int>>?>> =
        Array(height) { y -> Array(width) { x -> seatLinker(x, y, this) } }

    private fun isSeat(x: Int, y:Int): Boolean? {
        if (x < 0 || x >= width || y < 0 || y >= height) return null
        return seats[y][x] != null
    }

    private fun isOccupied(pair: Pair<Int, Int>) = seats[pair.second][pair.first] == true

    private val count get() = seats.sumBy { it.count { it == true } }
    private val isStable get() = willChange.all { it.all { !it } }


    private fun updateChanges(tolerance: Int) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val links = links[y][x] ?: continue
                val current = seats[y][x] ?: continue
                val count = links.count { isOccupied(it) }
                willChange[y][x] = if (current) {
                    count >= tolerance
                } else {
                    count == 0
                }
            }
        }
    }

    private fun commitChanges() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val current = seats[y][x] ?: continue
                if (willChange[y][x]) {
                    seats[y][x] = !current
                }
            }
        }
    }

    fun stabilise(tolerance: Int): Int {
        updateChanges(tolerance)
        while (!isStable) {
            commitChanges()
            updateChanges(tolerance)
        }
        return count
    }
}