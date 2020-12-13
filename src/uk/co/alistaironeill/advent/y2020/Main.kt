package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d13.ShuttleSearch
import java.io.File

fun main() {
    val data = File("data/day13.txt").readText()
    val result = ShuttleSearch.task2(data)
    println(result)
}