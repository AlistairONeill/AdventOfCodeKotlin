package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d19.MonsterMessages
import java.io.File

fun main() {
    val data = File("data/day19.txt").readText()
    val result = MonsterMessages.task2(data)
    println(result)
}
