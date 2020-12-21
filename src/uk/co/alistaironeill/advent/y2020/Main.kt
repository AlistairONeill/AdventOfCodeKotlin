package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d20.JurassicJigsaw
import java.io.File

fun main() {
    val data = File("data/day20.txt").readText()
    val jigsaw = JurassicJigsaw(data)
    val result = jigsaw.task2()
    println(result)
}
