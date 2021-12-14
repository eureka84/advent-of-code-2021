package org.erueka84

import org.erueka84.Common.readLines

object Day14 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day14.input")
        println(part1(input)) // 2797
    }

    private fun part1(input: Sequence<String>): Int = computeFinalPolymerScore(input, 10)

    private fun computeFinalPolymerScore(input: Sequence<String>, steps: Int): Int {
        val (initialPolymer, insertionRules) = parse(input)
        val finalPolymer = (1..steps).fold(initialPolymer) { acc, _ -> acc.apply(insertionRules) }
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

    infix fun String?.or(default: String): String = this?:default

    private fun Polymer.elementsCount(): Map<Element, Int> = this.groupingBy { it }.eachCount()

    private infix fun String.mergeWith(other: String): String = this + other.substring(1)

    data class InsertionRule(val pair: String, val charToInsert: String) {

        val replacement: String get() = pair[0] + charToInsert + pair[1]

        companion object {
            fun from(line: String): InsertionRule =
                line.split("->")
                    .map { it.trim() }
                    .let { (pair, charToInsert) -> InsertionRule(pair, charToInsert) }
        }
    }
}

typealias Polymer = String
typealias Element = Char