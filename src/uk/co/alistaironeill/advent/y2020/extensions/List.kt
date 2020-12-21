package uk.co.alistaironeill.advent.y2020.extensions

fun <E> List<E>.subList(startIndex: Int) = subList(startIndex, size)

fun <E, R: Comparable<R>> List<E>.minOf(selector: (E)->R) = selector(minBy(selector)!!)
fun <E, R: Comparable<R>> List<E>.maxOf(selector: (E)->R) = selector(maxBy(selector)!!)