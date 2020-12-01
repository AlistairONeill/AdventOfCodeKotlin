package uk.co.alistaironeill.advent.y2020.d01

import uk.co.alistaironeill.advent.y2020.extensions.subList

object ExpenseReport {
    /**
     * Returns the product of the n entries with sum to target.
     * Returns null if such a combination does not exist
     */
    private fun fix(n: Int, entries: List<Int>, target: Int): Int? {
        if (n == 0) { return if (target == 0) 1 else null }
        for (i in 0 until entries.size) {
            val entry = entries[i]
            return fix(n - 1, entries.subList(i + 1), target - entry)?.times(entry) ?: continue
        }

        return null
    }
}
