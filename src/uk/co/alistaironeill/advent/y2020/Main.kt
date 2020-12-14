package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d14.DockingData2
import java.io.File

fun main() {
    val data = File("data/day14.txt").readText()
    val result = DockingData2().process(data)
    println(result)
}