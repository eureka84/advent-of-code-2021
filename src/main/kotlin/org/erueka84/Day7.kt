package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.MathLib.mean
import org.erueka84.MathLib.median
import org.erueka84.MathLib.sumOfFirst
import kotlin.math.abs

object Day7 {

    @JvmStatic
    fun main(args: Array<String>) {
        val positions = readInitialCrabsPositions()
        println(part1(positions)) // 342641
        println(part2(positions)) // 93006301
    }

    private fun readInitialCrabsPositions(): List<Int> {
        val input = readLines("/day7.input")
        return input.first().split(",").map { it.toInt() }
    }

    private fun part1(positions: List<Int>): Int =
        positions
            .median()
            .let { median -> positions.sumOf { abs(it - median) } }

    private fun part2(positions: List<Int>): Int =
        positions
            .mean()
            .let { mean -> positions.sumOf { sumOfFirst(abs(it - mean)) } }

}

