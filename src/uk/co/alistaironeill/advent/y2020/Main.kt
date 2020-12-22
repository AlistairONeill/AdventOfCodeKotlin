package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d22.RecursiveCrabCombat
import java.io.File

fun main() {
    val data = File("data/day22.txt").readText()
    val cc = RecursiveCrabCombat.factory(data)
    cc.play()
    val result = cc.winnersScore
    println(result)
}