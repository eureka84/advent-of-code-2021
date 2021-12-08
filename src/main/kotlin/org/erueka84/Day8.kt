package org.erueka84

import org.erueka84.Common.readLines

object Day8 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day8.input")
        println(part1(input)) // 548
        println(part2(input))
    }

    private fun part1(input: Sequence<String>): Int {
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

    private fun part2(input: Sequence<String>): Int =
        input
            .map { line -> Display.from(line) }
            .sumOf { display -> display.reading }

    data class Display(
        private val scrambledReading: List<String>,
        private val scrambledUniqueDigits: List<String>
    ) {
        val reading: Int by lazy {
            scrambledReading.map { digitsMap[it] }.joinToString(separator = "").toInt()
        }

        private val digitsMap: Map<String, Char> by lazy {
            val byLength: Map<Int, List<String>> = scrambledUniqueDigits.groupBy { it.length }
            val result = mutableMapOf<String, Char>()

            val one = byLength[2]!!.first()
            val four = byLength[4]!!.first()
            val seven = byLength[3]!!.first()
            val eight = byLength[7]!!.first()

            result[one] = '1'
            result[four] = '4'
            result[seven] = '7'
            result[eight] = '8'

            extractDigitsOfLengthFive(byLength, result, one, four)
            extractDigitsOfLengthSix(byLength, result, one, four)

            result
        }

        private fun extractDigitsOfLengthSix(
            byLength: Map<Int, List<String>>,
            result: MutableMap<String, Char>,
            one: String,
            four: String
        ) {
            val oneOfZeroSixOrNine = byLength[6]!!
            val nine = oneOfZeroSixOrNine.find { it.containsAllCharsOf(four) }!!
            result[nine] = '9'

            val zeroOrSix = oneOfZeroSixOrNine - nine
            val zero = zeroOrSix.find { it.containsAllCharsOf(one) }!!
            result[zero] = '0'

            val six = (zeroOrSix - zero)[0]
            result[six] = '6'
        }

        private fun extractDigitsOfLengthFive(
            byLength: Map<Int, List<String>>,
            result: MutableMap<String, Char>,
            one: String,
            four: String
        ) {
            val oneOfTwoThreeOrFive = byLength[5]!!
            val three = oneOfTwoThreeOrFive.find { it.containsAllCharsOf(one) }!!
            result[three] = '3'

            val twoOrFive = oneOfTwoThreeOrFive - three
            val five = twoOrFive.find { it.containsAllCharsOf(four - one) }!!
            result[five] = '5'

            val two = (twoOrFive - five)[0]
            result[two] = '2'
        }

        companion object {
            fun from(line: String): Display {
                val (rawUniqueDigits, rawReading) = line.split("|")
                val scrambledReading = rawReading.trim().split(" ").map { sortChars(it) }
                val uniqueDigits = rawUniqueDigits.trim().split(" ").map { sortChars(it) }
                return Display(scrambledReading, uniqueDigits)
            }

            private fun sortChars(it: String) = it.toCharArray().sorted().joinToString(separator = "")
        }
    }

    private val DIGITS_WITH_UNIQUE_LENGTH_REPRESENTATION = listOf(1, 4, 7, 8)

    private val normalDigitsConf = mapOf(
        1 to "cf",     //     C     F   - 2
        7 to "acf",    // A   C     F   - 3
        4 to "bcdf",   //   B C D   F   - 4
        2 to "acdeg",  // A   C D E   G - 5
        3 to "acdfg",  // A   C D   F G - 5
        5 to "abdfg",  // A B   D   F G - 5
        0 to "abcefg", // A B C   E F G - 6
        6 to "abdefg", // A B   D E F G - 6
        9 to "abcdfg", // A B C D   F G - 6
        8 to "abcdefg" // A B C D E F G - 7
    )

    private operator fun String.minus(s: String): String {
        return s.toCharArray().fold(this) { acc: String, curr: Char -> acc.replace(curr.toString(), "") }
    }

    private fun String.containsAllCharsOf(other: String): Boolean = other.all { this.contains(it) }
}

