package uk.co.alistaironeill.advent.y2020.extensions

val Int.sqrt get(): Int {
    if (this < 0) {
        throw Exception("Not supporting negative integers")
    }
    var j = 0
    var i = 0
    while (j < this) {
        i += 1
        j = i * i
    }

    if (j == this) {
        return i
    }

    throw Exception("Only supporting perfect squares")
}