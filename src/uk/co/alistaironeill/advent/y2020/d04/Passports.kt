package uk.co.alistaironeill.advent.y2020.d04

object Passports {
    fun countValid(data: String, validator: (Passport) -> Boolean): Int =
        data.split("\n\n")
            .map { Passport.factory(it) }
            .count(validator)

    private val validatorGenerator: (List<(Passport) -> Boolean>) -> (Passport) -> Boolean = { validators ->
        { passport: Passport ->
            try {
                validators.all { it(passport) }
            } catch (e: Exception) {
                false
            }
        }
    }

    private val containsKeyValidator = { key: String ->
        { passport: Passport -> passport.containsKey(key) }
    }

    private val intRangeValidator = { field: String, min: Int, max: Int ->
        { passport: Passport -> passport[field]!!.toInt() in min .. max }
    }

    private val regexValidator = { regex: String, key: String ->
        { passport: Passport -> Regex(regex).matches(passport[key]!!)}
    }

    private val byrValidator = intRangeValidator("byr", 1920, 2002)
    private val iyrValidator = intRangeValidator("iyr", 2010, 2020)
    private val eyrValidator = intRangeValidator("eyr", 2020, 2030)
    private val hgtValidator = { passport: Passport ->
        val groupValues = Regex("^(\\d+)(\\w\\w)$").find(passport["hgt"]!!)!!.groupValues
        val number = groupValues[1].toInt()
        when (groupValues[2]) {
            "cm" -> number in 150 .. 193
            "in" -> number in 59 .. 76
            else -> false
        }
    }
    private val hclValidator = regexValidator("^#[0-9a-f]{6}$", "hcl")
    private val eclValidator = { passport: Passport -> listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(passport["ecl"]) }
    private val pidValidator = regexValidator("^\\d{9}$", "pid")

    val task1Validator = validatorGenerator(listOf(
        containsKeyValidator("byr"),
        containsKeyValidator("iyr"),
        containsKeyValidator("eyr"),
        containsKeyValidator("hgt"),
        containsKeyValidator("hcl"),
        containsKeyValidator("ecl"),
        containsKeyValidator("pid")
    ))

    val task2Validator: (Passport) -> Boolean = validatorGenerator( listOf (
        byrValidator,
        iyrValidator,
        eyrValidator,
        hgtValidator,
        hclValidator,
        eclValidator,
        pidValidator
    ))

    data class Passport(val fields: Map<String, String>) {
        companion object {
            fun factory(data: String) = Passport(
                data.split(Regex("[ \n]")).map {
                    val split = it.split(":")
                    split[0] to split[1]
                }.toMap()
            )
        }

        fun containsKey(key: String) = fields.containsKey(key)
        operator fun get(key: String) = fields[key]
    }
}