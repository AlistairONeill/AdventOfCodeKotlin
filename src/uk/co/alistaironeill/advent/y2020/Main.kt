package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d23.CrabCups
import java.io.File

fun main() {
    val data = File("data/day23.txt").readText()
    val cc = CrabCups(data)
    val result = cc.task2()
    println(result)
}