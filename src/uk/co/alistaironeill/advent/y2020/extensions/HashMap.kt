package uk.co.alistaironeill.advent.y2020.extensions

fun <K, V> HashMap<K, V>.containsAllKeys(keys: Iterable<K>): Boolean {
    for (key in keys) {
        if (!containsKey(key)) {
            return false
        }
    }
    return true
}