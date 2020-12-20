package uk.co.alistaironeill.advent.y2020.d19

class MonsterMessages(private val rawData: List<String>) {
    companion object {
        val characterRegex = Regex("\"\\w\"")

        fun task1(data: String): Int {
            return task(data)
        }

        fun task2(data: String): Int {
            return task(
                data,
                "8: 42" to "8: 42 | 8",
                "11: 42 31" to "11: 42 31 | 42 11 31"
            )
        }

        fun task(data: String, vararg replace: Pair<String, String>): Int {
            val split = data.split("\n\n")
            val rules = split[0].split("\n").filter { !replace.map { it.first}.contains(it) } + replace.map { it.second }
            val monsterMessages = MonsterMessages(rules)
            return monsterMessages.countValid(split[1].split("\n"))
        }
    }

    private val ruleData = HashMap<Int, String>()
    private val rules = HashMap<Int, String>()

    init {
        rawData.forEach {
            val split = it.split(": ")
            val key = split[0].toInt()
            val ruleDatum = split[1]
            ruleData[key] = ruleDatum
        }
    }

    private fun getRule(key: Int): String {
        var rule = rules[key]
        if (rule != null) {
            return rule
        }

        rule = generateRule(key)
        rules[key] = rule
        return rule
    }

    private fun generateRule(key: Int): String {
        val ruleDatum = ruleData[key] ?: throw Exception()

        if (key == 8 && ruleDatum == "42 | 8") {
            return rule8()
        }

        if (key == 11 && ruleDatum == "42 31 | 42 11 31") {
            return rule11()
        }

        return when {
            characterRegex.matches(ruleDatum) -> {
                ruleDatum[1].toString()
            }
            ruleDatum.contains("|") -> {
                "(?:${ruleDatum.split(" | ").joinToString("|"){ parseSubrule(it)}})"
            }
            else -> {
                parseSubrule(ruleDatum)
            }
        }
    }

    private fun rule8(): String {
        return "(${getRule(42)})+"
    }

    private fun rule11() : String {
        val rule42 = getRule(42)
        val rule31 = getRule(31)
        return "(?:${
            (1 until 100).joinToString("|") {
                "${rule42.repeat(it)}${rule31.repeat(it)}"
            }
        })"
    }

    private fun parseSubrule(data: String) = data.split(" ").joinToString("") { getRule(it.toInt()) }

    fun countValid(strings: List<String>): Int {
        val regex = Regex("^${getRule(0)}$")
        return strings.count { regex.matches(it) }
    }
}