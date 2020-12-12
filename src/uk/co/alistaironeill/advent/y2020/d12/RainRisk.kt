package uk.co.alistaironeill.advent.y2020.d12

import kotlin.math.*

object RainRisk {
    fun task1(input: String) = task(input, Ship1())
    fun task2(input: String) = task(input, Ship2())

    private fun task(input: String, ship: Ship): Double {
        input.split("\n").forEach {
            ship.takeAction(it)
        }
        return ship.manhatten
    }

    abstract class Ship {
        var x = 0.0
        var y = 0.0

        abstract fun takeAction(input: String)

        val manhatten get() = abs(x) + abs(y)
    }

    class Ship1: Ship() {
        private var direction = 0.0

        override fun takeAction(input:String) {
            val value = input.substring(1).toInt()
            when (input[0]) {
                'N' -> { y -= value }
                'S' -> { y += value }
                'E' -> { x += value }
                'W' -> { x -= value }
                'L' -> { direction -= value * PI / 180 }
                'R' -> { direction += value * PI / 180 }
                'F' -> {
                    x += value * cos(direction)
                    y += value * sin(direction)
                }
            }
        }
    }

    class Ship2: Ship() {
        private var wX = 10.0
        private var wY = 1.0

        private val wRad get() = atan2(wY, wX)
        private val wDist get() = sqrt(wX*wX + wY*wY)

        override fun takeAction(input: String) {
            val value = input.substring(1).toInt()
            when (input[0]) {
                'N' -> { wY += value }
                'S' -> { wY -= value }
                'E' -> { wX += value }
                'W' -> { wX -= value }
                'L' -> rotateWaypoint(value)
                'R' -> rotateWaypoint(-value)
                'F' -> {
                    x += wX * value
                    y += wY * value
                }
            }
        }

        private fun rotateWaypoint(deg: Int) {
            val rad = deg * PI / 180
            val wDist = wDist
            val newAngle = rad + wRad
            wX = wDist * cos(newAngle)
            wY = wDist * sin(newAngle)
        }
    }
}