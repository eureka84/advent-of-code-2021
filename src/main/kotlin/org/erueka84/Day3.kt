package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.CommonBits.BitCounts
import org.erueka84.CommonBits.asBinaryToInt

object Day3Part1 {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputLines = readLines("/day3.input")
        println(diagnosticReport(inputLines).powerConsumption) // 2035764
    }

    private fun diagnosticReport(inputLines: Sequence<String>): DiagnosticReport =
        DiagnosticReport(computeOverallBitCounts(inputLines))

    private fun computeOverallBitCounts(inputLines: Sequence<String>) =
        inputLines
            .map { it.map(BitCounts::from) }
            .reduce { acc, curr ->
                acc.zip(curr).map { (accBitCounts, lineBitCounts) -> accBitCounts + lineBitCounts }
            }

    class DiagnosticReport(private val bits: List<BitCounts>) {
        private val gammaRate: Int
            get() = bitsToInt { bitCounts -> bitCounts.mostFrequent }

        private val epsilonRate: Int
            get() = bitsToInt { bitCounts -> bitCounts.leastFrequent }

        private fun bitsToInt(bitSelector: (BitCounts) -> Char) =
            String(bits.map(bitSelector).toCharArray()).asBinaryToInt()

        val powerConsumption: Int get() = gammaRate * epsilonRate

    }

}

object Day3Part2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputLines = readLines("/day3.input").toList()
        println(lifeSupportRating(inputLines).value) // 2817661
    }

    private fun lifeSupportRating(inputLines: List<String>): LifeSupportRating =
        LifeSupportRating(computeOxygenRating(inputLines), computeCO2Rating(inputLines))

    private fun computeOxygenRating(inputLines: List<String>): Int =
        computeRating(inputLines, { bitCounts -> bitCounts.mostFrequent })

    private fun computeCO2Rating(inputLines: List<String>): Int =
        computeRating(inputLines, { bitCounts -> bitCounts.leastFrequent })

    private tailrec fun computeRating(
        inputLines: List<String>,
        selectBitValueFor: (BitCounts) -> Char,
        position: Int = 0
    ): Int {
        val bit = selectBitValueFor(bitCountsAtGivenPosition(inputLines, position))
        val filtered = inputLines.filter { it[position] == bit }
        if (filtered.size == 1) {
            return filtered.first().asBinaryToInt()
        } else {
            return computeRating(filtered, selectBitValueFor, position + 1)
        }
    }

    private fun bitCountsAtGivenPosition(inputLines: List<String>, position: Int) =
        inputLines
            .map { line -> BitCounts.from(line[position]) }
            .reduce(BitCounts::plus)

    data class LifeSupportRating(val oxygenRating: Int, val co2Rating: Int) {
        val value: Int get() = oxygenRating * co2Rating
    }

}

object CommonBits {
    data class BitCounts(val zeroCount: Int = 0, val oneCount: Int = 0) {

        val mostFrequent: Char = if (oneCount >= zeroCount) '1' else '0'
        val leastFrequent: Char = if (oneCount < zeroCount) '1' else '0'

        operator fun plus(other: BitCounts): BitCounts = BitCounts(
            zeroCount = zeroCount + other.zeroCount,
            oneCount = oneCount + other.oneCount
        )

        companion object {
            fun from(c: Char): BitCounts =
                if (c == '0') BitCounts(1, 0) else BitCounts(0, 1)

        }
    }

    fun String.asBinaryToInt(): Int = this.toInt(2)
}
