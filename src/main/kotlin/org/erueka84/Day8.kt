package org.erueka84

import org.erueka84.Common.readLines

object Day8 {

    private val DIGITS_WITH_UNIQUE_LENGTH_REPRESENTATION = listOf(1, 4, 7, 8)

    private val normalDigitsConf = mapOf(
        0 to "abcefg",
        1 to "cf",
        2 to "acdeg",
        3 to "acdfg",
        4 to "bcdf",
        5 to "abdfg",
        6 to "abdefg",
        7 to "acf",
        8 to "abcdefg",
        9 to "abcdfg"
    )

    @JvmStatic
    fun main(args: Array<String>) {
        println(part1()) // 548
    }

    private fun part1(): Int {
        val input = readLines("/day8.input")
        val readingDigitsByRepresentationSize = readingDigits(input).groupingBy { it.length }.eachCount()

        return DIGITS_WITH_UNIQUE_LENGTH_REPRESENTATION
            .map { normalDigitsConf[it]?.length }
            .sumOf { readingDigitsByRepresentationSize[it]!! }
    }

    private fun readingDigits(input: Sequence<String>) = input.flatMap { line ->
        val (_, reading) = line.split("|")
        val readingDigits = reading.trim().split(" ")
        readingDigits
    }
}