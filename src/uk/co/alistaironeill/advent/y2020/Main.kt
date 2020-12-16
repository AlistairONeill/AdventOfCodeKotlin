package uk.co.alistaironeill.advent.y2020

import uk.co.alistaironeill.advent.y2020.d16.TicketTranslation
import java.io.File

fun main() {
    val data = File("data/day16.txt").readText()
    val ticketTranslation = TicketTranslation(data)
    ticketTranslation.solveSchema()
    val result = ticketTranslation.getResult()
    println(result)
}