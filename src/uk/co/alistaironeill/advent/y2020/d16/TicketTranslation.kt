package uk.co.alistaironeill.advent.y2020.d16

class TicketTranslation(raw: String) {
    private val schema = HashMap<String, NumberValidator>()
    private val myTicket: Ticket
    private val validTickets = ArrayList<Ticket>()
    private val invalidTickets = ArrayList<Ticket>()

    private val possibleSlots = HashMap<String, List<Int>>()

    init {
        val split = raw.split("\n")
        var i = 0
        while (split[i].isNotBlank()) {
            val subSplit = split[i].split(": ")
            schema[subSplit[0]] = NumberValidator(subSplit[1])
            i += 1
        }
        
        i += 2
        
        myTicket = Ticket(split[i])
        
        i += 3
        while (i < split.size) {
            val ticket = Ticket(split[i])
            if (ticket.numbers.any { number -> schema.values.all { !it.isValid(number) } }) {
                invalidTickets.add(ticket)
            }
            else {
                validTickets.add(ticket)
            }
            i += 1
        }

        validTickets.add(myTicket)

        val slots = myTicket.numbers.size
        schema.keys.forEach {
            possibleSlots[it] = (0 until slots).toList()
        }
    }
    
    fun sumOfInvalidValues(): Int {
        return invalidTickets.sumBy { ticket ->
            ticket.numbers.sumBy { number ->
                if (schema.values.none { it.isValid(number) }) number else 0
            }
        }
    }

    fun solveSchema(): Boolean {
        for (name in possibleSlots.keys) {
            val validator = schema[name]!!
            possibleSlots[name] = possibleSlots[name]!!.filter { slot ->
                validTickets.all { ticket ->
                    validator.isValid(ticket.numbers[slot])
                }
            }
        }

        while (possibleSlots.values.any { it.size > 1}) {
            narrowUniques()
        }

        return true
    }

    private fun narrowUniques() {
        for (name in possibleSlots.keys) {
            val slots = possibleSlots[name]!!
            if (slots.size == 1) {
                val slot = slots.first()
                for (name2 in possibleSlots.keys) {
                    if (name == name2) continue
                    possibleSlots[name2] = possibleSlots[name2]!!.filter { it != slot }
                }
            }
        }
    }

    fun getResult(): Long {
        var prod = 1L
        possibleSlots.forEach {
            if (it.key.startsWith("departure")) {
                prod *= myTicket.numbers[it.value.first()]
            }
        }
        return prod
    }


    class NumberValidator(raw: String) {
        private val ranges: List<IntRange> = raw.split(" or ").map {
            val split = it.split("-")
            split[0].toInt() .. split[1].toInt()
        }

        fun isValid(number: Int) = ranges.any { number in it }
    }

    class Ticket(raw: String) {
        val numbers = raw.split(",").map { it.toInt() }
    }
}