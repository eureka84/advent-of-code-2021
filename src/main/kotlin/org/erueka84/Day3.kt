package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.CommonBits.BitCounts
import org.erueka84.CommonBits.BitCounts.Companion.toBitCount

object CommonBits {
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

            fun Char.toBitCount(): BitCounts = from(this)
        }
    }
}


object Day3Part1 {

    class Report(private val bits: List<BitCounts>) {
        private val gamma: Int
            get() = bitsStringToInt { bitCounts -> bitCounts.mostFrequent }

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
        val inputLines = readLines("/day3.input").toList()
        println(compute(inputLines).score)
    }

    private fun compute(inputLines: List<String>): Result {
        return Result(computeOxygenRating(inputLines), computeCO2Rating(inputLines))
    }

    private fun computeOxygenRating(inputLines: List<String>): Int {
        return computeRating(inputLines, { bitCounts ->
            if (bitCounts.oneCount >= bitCounts.zeroCount)
                '1'
            else
                '0'
        })
    }

    private fun computeCO2Rating(inputLines: List<String>): Int {
        return computeRating(inputLines, { bitCounts ->
            if (bitCounts.oneCount < bitCounts.zeroCount)
                '1'
            else
                '0'
        })
    }

    private tailrec fun computeRating(
        inputLines: List<String>,
        bitCriteria: (BitCounts) -> Char,
        position: Int = 0
    ): Int{
        val reduce = inputLines.map { it[position].toBitCount() }.reduce(BitCounts::plus)
        val bit = bitCriteria(reduce)
        val filtered = inputLines.filter { it[position] == bit }
        if (filtered.size == 1) {
            return filtered.first().toInt(2)
        } else {
            return computeRating(filtered, bitCriteria, position + 1)
        }
    }

    data class Result(val oxygenRating: Int, val co2Rating: Int) {
        val score: Int get() = oxygenRating * co2Rating
    }
}