package org.erueka84

import org.erueka84.Common.readLines

val sonarReads = readLines("/day1.input").map { it.toInt() }

fun puzzle1(): Int =
    sonarReads
        .zip(sonarReads.drop(1))
        .count { (prev, next) -> next > prev }


fun puzzle2(): Int =
    sonarReads.windowed(3).let { windowed ->
        windowed
            .zip(windowed.drop(1))
            .count { (prev, next) -> next.sum() > prev.sum() }
    }

fun puzzle2Bis(): Int =
    sonarReads
        .zip(sonarReads.drop(3))
        .count { (prev, next) -> next > prev }

fun main() {
    println(puzzle1())
    println(puzzle2())
    println(puzzle2Bis())
}
