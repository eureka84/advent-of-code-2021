package org.erueka84

import org.erueka84.Common.readLines

object Day3Part1 {

    data class BitCounts(val zeroCount: Int = 0, val oneCount: Int = 0) {

        val mostFrequent: String = if (zeroCount > oneCount) "0" else "1"
        val leastFrequent: String = if (zeroCount < oneCount) "0" else "1"

        operator fun plus(other: BitCounts): BitCounts = BitCounts(
            zeroCount = zeroCount + other.zeroCount,
            oneCount = oneCount + other.oneCount
        )

        companion object {
            fun from(c: Char): BitCounts =
                if (c == '0') BitCounts(1, 0) else BitCounts(0, 1)
        }
    }

    class Report(private val bits: List<BitCounts>) {
        private val gamma: Int
            get() = bitsStringToInt { bitCounts ->  bitCounts.mostFrequent }

        private val epsilon: Int
            get() = bitsStringToInt { bitCounts -> bitCounts.leastFrequent }

        private fun bitsStringToInt(transform: (BitCounts) -> CharSequence) =
            bits.joinToString(separator = "", transform = transform).toInt(2)

        val score: Int get() = gamma * epsilon
    }

    private fun compute(inputLines: Sequence<String>): Report {
        val overallBitsCounts: List<BitCounts> =
            inputLines
                .map { it.map(BitCounts::from) }
                .reduce { acc, curr -> acc.zip(curr).map { (accBit, currBit) -> accBit + currBit } }

        return Report(overallBitsCounts)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val inputLines = readLines("/day3.input")
        println(compute(inputLines).score)
    }
}

object Day3Part2 {

    @JvmStatic
    fun main(args: Array<String>) {
        val inputLines = readLines("/day3.input")
        println(compute(inputLines).score())
    }

    private fun compute(inputLines: Sequence<String>): Result {
        TODO()
    }

    class Result {
        fun score(): Int = TODO()
    }
}