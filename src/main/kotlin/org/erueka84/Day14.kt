package org.erueka84

import org.erueka84.Common.readLines
import java.util.*
import java.util.stream.Collectors


object Day14Part1 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day14.input")
        println(solve(input)) // 2797
    }

    private fun solve(input: Sequence<String>): Int {
        val (initialPolymer, insertionRules) = parse(input)
        val finalPolymer = (1..10).fold(initialPolymer) { acc, _ -> acc.apply(insertionRules) }
        val elementsCount = finalPolymer.elementsCount()
        val maxOf: Int = elementsCount.maxOf { (_, v) -> v }
        val minOf: Int = elementsCount.minOf { (_, v) -> v }
        return maxOf - minOf
    }

    private fun parse(input: Sequence<String>): Pair<Polymer, List<InsertionRule>> {
        val initialPolymer: Polymer = input.first()
        val insertionRules = input.drop(2).map { line -> InsertionRule.from(line) }.toList()
        return Pair(initialPolymer, insertionRules)
    }

    private fun Polymer.apply(insertionRules: List<InsertionRule>): Polymer {
        val rulesByPair = insertionRules.groupBy { it.pair }.mapValues { (_, v) -> v.first() }
        return this.windowed(2)
            .map { pair -> rulesByPair[pair]?.replacement or pair }
            .reduce { acc, s -> acc mergeWith s }
    }

    infix fun String?.or(default: String): String = this ?: default

    private fun Polymer.elementsCount(): Map<Element, Int> = this.groupingBy { it }.eachCount()

    private infix fun String.mergeWith(other: String): String = this + other.substring(1)

}

object Day14Part2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day14.input")
        println(solve(input)) // 2797
    }

    private fun solve(input: Sequence<String>): Long {
        val (initialPolymer, insertionRules) = parse(input)

        val lastPolymer = (1..40).fold(initialPolymer) { acc, _ -> acc.applyRules(insertionRules) }

        return lastPolymer.score()
    }

    private fun parse(input: Sequence<String>): Pair<Map<String, Long>, Map<String, String>> {
        val initialPolymer: Map<String, Long> =
            input.first().windowed(2).groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }

        val insertionRules = input.drop(2).map { line -> parseRule(line) }.toMap()

        return Pair(initialPolymer, insertionRules)
    }

    private fun parseRule(line: String): Pair<String, String> {
        val (_, pair, insertion) = "([A-z]{2})\\s+->\\s([A-Z])".toRegex().matchEntire(line)!!.groupValues
        return Pair(pair, insertion)
    }

    private fun Map<String, Long>.applyRules(rules: Map<String, String>): Map<String, Long> {
        val newPoly = HashMap<String, Long>()
        this.forEach{ pair ->
            if (pair.key.length == 2) {
                val midChar: String = rules[pair.key]!!
                val leftPair = "${pair.key[0]}$midChar"
                val rightPair = "$midChar${pair.key[1]}"
                newPoly.merge(
                    leftPair, pair.value
                ) { a: Long?, b: Long? -> java.lang.Long.sum(a!!, b!!) }
                newPoly.merge(
                    rightPair, pair.value
                ) { a: Long?, b: Long? -> java.lang.Long.sum(a!!, b!!) }
            } else {
                newPoly[pair.key] = pair.value
            }
        }
        return newPoly
    }

    private fun Map<String, Long>.score(): Long {
        val stats: LongSummaryStatistics = this.entries.stream()
            .collect(
                Collectors.groupingBy(
                    { e -> e.key[0] },
                    Collectors.summingLong{ it.value }
                )
            )
            .values
            .stream()
            .collect(Collectors.summarizingLong {it.toLong()})
        return stats.max - stats.min
    }
}


data class InsertionRule(val pair: String, val charToInsert: String) {

    val replacement: String get() = pair[0] + charToInsert + pair[1]

    companion object {
        fun from(line: String): InsertionRule =
            line.split("->")
                .map { it.trim() }
                .let { (pair, charToInsert) -> InsertionRule(pair, charToInsert) }
    }
}

typealias Polymer = String
typealias Element = Char