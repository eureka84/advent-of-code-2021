package org.erueka84

import java.io.File

val sonarReads = File("src/main/resources/day1.input").readLines().map { it.toInt() }

fun puzzle1(): Int {
    return sonarReads.take(sonarReads.size - 1)
        .zip(sonarReads.drop(1))
        .fold(0) { acc, (prev, next) -> if (next > prev) acc + 1 else acc }
}


fun puzzle2(): Int {
    val windowed: List<List<Int>> = sonarReads.windowed(3)

    return windowed.take(windowed.size - 1)
        .zip(windowed.drop(1))
        .fold(0) { acc, (prev: List<Int>, next: List<Int>) ->
            if (next.sum() > prev.sum())
                acc + 1
            else
                acc
        }
}

fun puzzle2Bis(): Int {
    return sonarReads.take(sonarReads.size - 3)
        .zip(sonarReads.drop(3))
        .fold(0) { acc, (prev, next) -> if (next > prev) acc + 1 else acc }
}

fun main() {
    println(puzzle1())
    println(puzzle2())
    println(puzzle2Bis())
}
