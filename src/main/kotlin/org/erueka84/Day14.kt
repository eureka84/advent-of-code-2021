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
        println(solve(input)) // 2926813379532
    }

    private fun solve(input: Sequence<String>): Long {
        val (initialPolymer, insertionRules) = parse(input)

        val lastPolymer = (1..40).fold(initialPolymer) { acc, _ -> acc.applyRules(insertionRules) }

        return lastPolymer.score()
    }

    private fun parse(input: Sequence<String>): Pair<Map<String, Long>, Map<String, String>> {
        val template = input.first()
        val initialPolymer: Map<String, Long> =
            template
                .windowed(2)
                .groupingBy { it }
                .eachCount()
                .mapValues { (_, v) -> v.toLong() }
                .plus(template.last().toString() to 1)

        val insertionRules = input.drop(2).map { line -> parseRule(line) }.toMap()

        return Pair(initialPolymer, insertionRules)
    }

    private fun parseRule(line: String): Pair<String, String> {
        val pattern = "([A-z]{2})\\s+->\\s([A-Z])".toRegex()
        val (_, pair, insertion) = pattern.matchEntire(line)!!.groupValues
        return Pair(pair, insertion)
    }

    private fun Map<String, Long>.applyRules(rules: Map<String, String>): Map<String, Long> =
        flatMap { (k, v) ->
            val middle: String? = rules[k]
            if (middle != null) {
                val leftPair = k[0] + middle
                val rightPair = middle + k[1]
                listOf(Pair(leftPair, v), Pair(rightPair, v))
            } else {
                listOf(Pair(k, v))
            }
        }.groupingBy{ it.first }.aggregate { _, acc, el, _ -> (acc?:0) + el.second }

    private fun Map<String, Long>.score(): Long {
        val aggregate: Map<Char, Long> =
            this.entries
                .groupingBy { it.key.first() }
                .aggregate { _, accumulator, entry, _ -> (accumulator ?: 0) + entry.value }

        return aggregate.maxOf { it.value } - aggregate.minOf { it.value }
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