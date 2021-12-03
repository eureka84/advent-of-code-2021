package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.CommonBits.BitCounts
import org.erueka84.CommonBits.BitCounts.Companion.toBitCount
import org.erueka84.CommonBits.asBinaryToInt

object Day3Part1 {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputLines = readLines("/day3.input")
        println(diagnosticReport(inputLines).powerConsumption)
    }

    private fun diagnosticReport(inputLines: Sequence<String>): DiagnosticReport {
        val overallBitsCounts: List<BitCounts> =
            inputLines
                .map { it.map(BitCounts::from) }
                .reduce { acc, curr -> acc.zip(curr).map { (accBitCounts, lineBitCounts) -> accBitCounts + lineBitCounts } }

        return DiagnosticReport(overallBitsCounts)
    }

    class DiagnosticReport(private val bits: List<BitCounts>) {
        private val gammaRate: Int
            get() = bitsToInt { bitCounts -> bitCounts.mostFrequent }

        private val epsilonRate: Int
            get() = bitsToInt { bitCounts -> bitCounts.leastFrequent }

        private fun bitsToInt(bitSelector: (BitCounts) -> CharSequence) =
            bits.joinToString(separator = "", transform = bitSelector).asBinaryToInt()

        val powerConsumption: Int get() = gammaRate * epsilonRate

    }

}

object Day3Part2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputLines = readLines("/day3.input").toList()
        println(lifeSupportRating(inputLines).value)
    }

    private fun lifeSupportRating(inputLines: List<String>): LifeSupportRating {
        return LifeSupportRating(computeOxygenRating(inputLines), computeCO2Rating(inputLines))
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
        bitValueSelection: (BitCounts) -> Char,
        position: Int = 0
    ): Int{
        val bitCountsAtGivenPosition = inputLines.map { it[position].toBitCount() }.reduce(BitCounts::plus)
        val bit = bitValueSelection(bitCountsAtGivenPosition)
        val filtered = inputLines.filter { it[position] == bit }
        if (filtered.size == 1) {
            return filtered.first().asBinaryToInt()
        } else {
            return computeRating(filtered, bitValueSelection, position + 1)
        }
    }

    data class LifeSupportRating(val oxygenRating: Int, val co2Rating: Int) {
        val value: Int get() = oxygenRating * co2Rating
    }

}

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

    fun String.asBinaryToInt(): Int = this.toInt(2)
}
