package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d21.AllergenAssessment
import java.io.File

fun main() {
    val data = File("data/day21.txt").readText()
    val aa = AllergenAssessment(data)
    val result = aa.task2()
    println(result)
}
