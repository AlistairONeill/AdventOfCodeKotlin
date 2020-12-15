package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d15.MemoryGame
import java.io.File

fun main() {
    val data = File("data/day15.txt").readText().split(",").map { it.toLong() }
    val result = MemoryGame(data).getNth(30000000)
    println(result)
}