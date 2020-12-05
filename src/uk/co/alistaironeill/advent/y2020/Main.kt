package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d05.BoardingPasses
import java.io.File

fun main() {
    val data = File("data/day5.txt").readText().split("\n")
    val missingId = BoardingPasses.missingSeat(data)
    println(missingId)
}