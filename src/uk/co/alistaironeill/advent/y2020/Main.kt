package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d09.XMAS
import java.io.File

fun main() {
    val data = File("data/day9.txt").readText().split("\n").map{it.toLong()}
    val result = XMAS.findWeakness(data, 25)

    println(result)
}