package uk.co.alistaironeill.advent.y2020.d07

import uk.co.alistaironeill.advent.y2020.extensions.containsAllKeys
import java.io.File

class Haversacks(rawRules: String) {
    companion object {
        private val numberRegex = Regex("^\\d+")
        private val bagNameRegex = Regex("[a-zA-Z]+ [a-zA-Z]+")
        private const val noOtherBags = "no other bags"

        private val bagName = { raw: String -> bagNameRegex.find(raw)!!.value }
        private val contents = { raw: String ->
            try {
                bagNameRegex.find(raw)!!.value to numberRegex.find(raw)!!.value.toInt()
            }
            catch (e: Exception) {
                println(raw)
                throw e
            }
        }

        private val rule = { raw: String ->
            val split = raw.substring(0, raw.length - 1).split (" contain ")
            val container = bagName(split[0])
            val contents = if (split[1] == noOtherBags) {
                HashMap()
            }
            else {
                split[1].split(", ").map { contents(it) }.toMap()
            }
            container to contents
        }
    }

    private val contents: Map<String, Map<String, Int>> = rawRules.split("\n").map { rule(it) }.toMap()

    private fun canContain(parent: String, child: String): Boolean {
        val contents = contents[parent]!!
        return when {
            contents.isEmpty() -> false
            contents.containsKey(child) -> true
            else -> contents.any { canContain(it.key, child) }
        }
    }

    fun possibleContainerCount(type: String) = contents.keys.filter { canContain(it, type) }.count()

    private fun totalContents(type: String): Int {
        return 1 + contents[type]!!.entries.sumBy { it.value * totalContents(it.key) }
    }

    fun contents(type: String): Int {
        return totalContents(type) - 1
    }
}