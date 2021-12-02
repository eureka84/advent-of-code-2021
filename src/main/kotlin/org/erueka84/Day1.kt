package org.erueka84

import org.erueka84.Common.readLines

object Day1{
    private val sonarReads: Sequence<Int> = readLines("/day1.input").map { it.toInt() }

    private fun puzzle1(): Int =
        sonarReads
            .zip(sonarReads.drop(1))
            .count { (prev, next) -> next > prev }


    private fun puzzle2(): Int =
        sonarReads.windowed(3).let { windows ->
            windows
                .zip(windows.drop(1))
                .count { (prev, next) -> next.sum() > prev.sum() }
        }

    private fun puzzle2Bis(): Int =
        sonarReads
            .zip(sonarReads.drop(3))
            .count { (prev, next) -> next > prev }

    fun main() {
        println(puzzle1())
        println(puzzle2())
        println(puzzle2Bis())
    }
}

