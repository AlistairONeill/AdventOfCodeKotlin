package uk.co.alistaironeill.advent.y2020.d21

class AllergenAssessment(raw: String) {
    val clues: List<Clue>
    val ingredients: List<String>
    val allergens: List<String>

    val allergenToIngredient = HashMap<String, String>()

    val unsolvedAllergens get() = allergens.filter { !allergenToIngredient.containsKey(it) }
    val unsolvedIngredients get() = ingredients.filter { !allergenToIngredient.containsValue(it) }

    init {
        clues = raw.split("\n").map { Clue(it) }

        val ingredients = ArrayList<String>()
        val allergens = ArrayList<String>()

        clues.forEach { clue ->
            ingredients.addAll(clue.ingredients)
            allergens.addAll(clue.allergens)
        }

        this.ingredients = ingredients.distinct()
        this.allergens = allergens.distinct()
    }

    fun solve() {
        while (unsolvedAllergens.isNotEmpty()) {
            if (!findNext()) {
                throw Exception()
            }
        }
    }

    fun task1(): Int {
        solve()

        val unsolvedIngredients = unsolvedIngredients
        return clues.sumBy {
            it.ingredients.count { unsolvedIngredients.contains(it) }
        }
    }

    fun task2(): String {
        solve()
        return allergenToIngredient.entries.toList().sortedBy { it.key }.joinToString(","){ it.value }
    }

    fun findNext(): Boolean {
        val unsolvedAllergens = unsolvedAllergens
        val unsolvedIngredients = unsolvedIngredients
        for (allergen in unsolvedAllergens) {
            val relevant = clues.filter { it.allergens.contains(allergen) }
            val filtered = relevant.fold(unsolvedIngredients) { possible, clue ->
                possible.filter { clue.ingredients.contains(it) }
            }

            if (filtered.size == 1){
                val ingredient = filtered[0]
                allergenToIngredient[allergen] = ingredient
                return true
            }
        }
        return false
    }

    class Clue(raw: String) {
        val ingredients: List<String>
        val allergens: List<String>

        init {
            val split = raw.split(" (contains ")
            ingredients = split[0].split(" ")
            allergens = split[1].substring(0, split[1].length - 1).split(", ")
        }
    }
}