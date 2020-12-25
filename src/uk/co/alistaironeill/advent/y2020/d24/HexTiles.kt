package uk.co.alistaironeill.advent.y2020.d24

import uk.co.alistaironeill.advent.y2020.extensions.even
import uk.co.alistaironeill.advent.y2020.extensions.maxOf

class HexTiles(data: String) {
    private val commands = data.split("\n").map { it.toList() }
    private val reference: Tile
    private val tiles: Array<Array<Tile>>

    init {
        val max = commands.maxOf { it.size } * 2 + 202

        tiles = Array(max){Array(max){ Tile() } }

        //Link the tiles
        for (y in tiles.indices) {
            for (x in tiles[y].indices) {
                val tile = tiles[y][x]

                try {
                    val e = tiles[y][x+1]
                    tile.e = e
                    e.w = tile
                }
                catch (e: Exception) { }

                try {
                    val se = if (y.even) {
                        tiles[y+1][x+1]
                    }
                    else {
                        tiles[y+1][x]
                    }

                    tile.se = se
                    se.nw = tile
                }
                catch (e: Exception) { }

                try {
                    val sw = if (y.even) {
                        tiles[y+1][x]
                    }
                    else {
                        tiles[y+1][x-1]
                    }

                    tile.sw = sw
                    sw.ne = tile
                }
                catch (e: Exception) { }
            }
        }

        reference = tiles[max/2][max/2]
    }

    fun task1(): Int {
        performCommands()
        return count()
    }

    fun task2(): Int {
        performCommands()
        lockInLinks()
        repeat(100) {
            takeTurn()
        }
        return count()
    }

    private fun performCommands() {
        commands.forEach {
            reference.flip(it)
        }
    }

    private fun count() = tiles.sumBy {
        it.count {
            it.black
        }
    }

    private fun lockInLinks() {
        tiles.forEach {
            it.forEach {
                it.lockInLinks()
            }
        }
    }

    private fun takeTurn() {
        findNew()
        lockInNew()
    }

    private fun findNew() {
        tiles.forEach {
            it.forEach {
                it.findNew()
            }
        }
    }

    private fun lockInNew() {
        tiles.forEach {
            it.forEach {
                it.lockInNew()
            }
        }
    }


    class Tile {
        var black = false
        var new = false

        var e: Tile? = null
        var w: Tile? = null
        var nw: Tile? = null
        var ne: Tile? = null
        var sw: Tile? = null
        var se: Tile? = null

        lateinit var surrounding: List<Tile>

        val count get() = surrounding.count { it.black }

        fun flip(route: List<Char>) {
            if (route.isEmpty()) {
                black = !black
                return
            }

            when (route[0]) {
                'e' -> e!!.flip(route.drop(1))
                'w' -> w!!.flip(route.drop(1))
                'n' -> when (route[1]) {
                    'e' -> ne!!.flip(route.drop(2))
                    'w' -> nw!!.flip(route.drop(2))
                }
                's' -> when (route[1]) {
                    'e' -> se!!.flip(route.drop(2))
                    'w' -> sw!!.flip(route.drop(2))
                }
            }
        }

        fun lockInLinks() {
            surrounding = listOfNotNull(e, w, nw, ne, sw, se)
        }

        fun findNew() {
            val count = count
            new = if (black) {
                (count == 1 || count == 2)
            }
            else {
                (count == 2)
            }
        }

        fun lockInNew() {
            black = new
        }
    }
}