package org.erueka84

import org.erueka84.Common.readLines

object Day14{
    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day14.input")
        val (initialPolymer, insertionRules) = parse(input)
        println(solve(initialPolymer, insertionRules, 10)) // 2797
        println(solve(initialPolymer, insertionRules, 40)) // 2926813379532
    }

    private fun solve(initialPolymer: Map<String, Long>, insertionRules: Map<String, String>, steps: Int): Long =
        (1..steps)
            .fold(initialPolymer) { currentPolymer, _ -> currentPolymer applyRules insertionRules }
            .score()

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

    private infix fun Map<String, Long>.applyRules(rules: Map<String, String>): Map<String, Long> =
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