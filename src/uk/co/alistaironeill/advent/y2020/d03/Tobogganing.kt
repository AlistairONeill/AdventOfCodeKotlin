package uk.co.alistaironeill.advent.y2020.d03

object Tobogganing {
    fun productOfTreesHit(slopes: List<Slope>, treeMap: TreeMap) =
        slopes.map { treesHit(it, treeMap) }
            .reduce { acc, i -> acc * i }

    fun treesHit(slope: Slope, treeMap: TreeMap): Long {
        var position = Position(0, 0)
        var count = 0L
        while (position.y < treeMap.height) {
            if (treeMap[position]) count += 1
            position = position.move(slope)
        }
        return count
    }

    data class Position(val x: Int, val y: Int) {
        fun move(slope: Slope) = Position(x + slope.dX, y + slope.dY)
    }

    data class Slope(val dX: Int, val dY: Int)

    data class TreeMap(val data: List<List<Boolean>>) {
        companion object {
            fun factory(raw: String) = factory(raw.split("\n"))
            fun factory(lines: List<String>) = TreeMap(
                lines.map { line ->
                    line.map { char ->
                        when(char) {
                            '.' -> false
                            '#' -> true
                            else -> throw Exception("Badly formatted!")
                        }
                    }
                }
            )
        }

        val height = data.size
        private val width = data[0].size

        operator fun get(pos: Position) = data[pos.y][pos.x % width]
    }
}
