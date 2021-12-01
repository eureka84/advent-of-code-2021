package org.erueka84

import java.io.File

val sonarReads = File("src/main/resources/day1.input").readLines().map { it.toInt() }

fun puzzle1(): Int =
    sonarReads.take(sonarReads.size - 1)
        .zip(sonarReads.drop(1))
        .count { (prev, next) -> next > prev }


fun puzzle2(): Int =
    sonarReads.windowed(3).let { windowed ->
        windowed.take(windowed.size - 1)
            .zip(windowed.drop(1))
            .count { (prev, next) -> next.sum() > prev.sum() }
    }

fun puzzle2Bis(): Int =
    sonarReads.take(sonarReads.size - 3)
        .zip(sonarReads.drop(3))
        .count { (prev, next) -> next > prev }

fun main() {
    println(puzzle1())
    println(puzzle2())
    println(puzzle2Bis())
}
