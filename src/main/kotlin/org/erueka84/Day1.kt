package org.erueka84

import org.erueka84.Common.readLines

object Day1{
    private val sonarReads: Sequence<Int> = readLines("/day1.input").map { it.toInt() }

    private fun part1(): Int =
        sonarReads
            .zip(sonarReads.drop(1))
            .count { (prev, next) -> next > prev }


    private fun part2(): Int =
        sonarReads.windowed(3).let { windows ->
            windows
                .zip(windows.drop(1))
                .count { (prev, next) -> next.sum() > prev.sum() }
        }

    private fun part2Improved(): Int =
        sonarReads
            .zip(sonarReads.drop(3))
            .count { (prev, next) -> next > prev }

    @JvmStatic
    fun main(args: Array<String>) {
        println(part1())
        println(part2())
        println(part2Improved())
    }
}

