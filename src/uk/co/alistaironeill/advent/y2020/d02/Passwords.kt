package uk.co.alistaironeill.advent.y2020.d02

object Passwords {
    private fun countValid(data: String, schema: PasswordValidator.Schema) = countValid(data.split("\n"), schema)
    private fun countValid(data: List<String>, schema: PasswordValidator.Schema) = data.count { isValid(it, schema) }

    private fun isValid(line: String, schema: PasswordValidator.Schema): Boolean {
        val split = line.split(": ")
        return PasswordValidator(split[0], schema).passes(split[1])
    }

    private class PasswordValidator(desc: String, schema: Schema) {
        private val num1: Int
        private val num2: Int
        private val char: Char
        val passes: (String) -> Boolean

        init {
            val spaceSplit = desc.split(" ")
            char = spaceSplit[1][0]
            val hyphenSplit = spaceSplit[0].split("-")
            num1 = hyphenSplit[0].toInt()
            num2 = hyphenSplit[1].toInt()
            passes = when (schema) {
                Schema.Count -> { password: String ->
                    password.count{ it == char } in num1..num2
                }
                Schema.Positions -> { password: String ->
                    (password[num1 - 1] == char && password[num2 - 1] != char)
                            || (password[num1 - 1] != char && password[num2 - 1] == char)
                }
            }
        }

        enum class Schema {
            Count,
            Positions
        }
    }
}
