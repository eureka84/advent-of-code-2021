package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.Day10.ValidationResult.*
import java.util.*

object Day10 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day10.input")
        println(part1(input)) // 344193
        println(part2(input)) // 3241238967
    }

    private fun part1(input: Sequence<String>): Long =
        input.map { validate(it) }
            .filter { it.isCorrupted() }
            .fold(0L) { acc, curr -> acc + curr.corruptedLinesScore() }

    private fun part2(input: Sequence<String>): Long =
        input.map { validate(it) }
            .filter { it.isIncomplete() }
            .map { it.incompleteLinesScore() }
            .middle()

    fun validate(s: String): ValidationResult {
        if (s.isEmpty()) return Valid

        val openParentheses: Deque<Char> = LinkedList()
        s.toCharArray().forEach { c ->
            if (isOpenParentheses(c)) {
                openParentheses.push(c)
            } else {
                if (openParentheses.isEmpty()) {
                    return Corrupted(c)
                }
                val lastOpenParentheses = openParentheses.pop()
                if (parenthesesDictionary[lastOpenParentheses] != c) {
                    return Corrupted(c)
                }
            }
        }
        if (openParentheses.isEmpty())
            return Valid
        else
            return Incomplete(openParentheses)
    }

    private fun isOpenParentheses(c: Char) = parenthesesDictionary.containsKey(c)

    private val parenthesesDictionary = mapOf('{' to '}', '[' to ']', '(' to ')', '<' to '>')

    private fun Sequence<Long>.middle(): Long {
        val list = this.toList().sorted()
        return list[list.size/2]
    }

    sealed class ValidationResult {
        fun isIncomplete(): Boolean = when (this) {
            is Incomplete -> true
            else -> false
        }

        fun isCorrupted(): Boolean = when (this) {
            is Corrupted -> true
            else -> false
        }

        fun corruptedLinesScore(): Long = when (this) {
            is Valid, is Incomplete -> 0
            is Corrupted -> wrongParentheses.corruptedScore()
        }

        fun incompleteLinesScore(): Long = when (this) {
            is Valid, is Corrupted -> 0L
            is Incomplete -> parenthesesToComplete.fold(0L) { acc, curr -> 5 * acc + curr.incompleteScore() }
        }

        private fun Char.corruptedScore(): Long = when (this) {
            ')' -> 3L
            ']' -> 57L
            '}' -> 1197L
            '>' -> 25137L
            else -> 0L
        }

        private fun Char.incompleteScore(): Long = when (this) {
            ')' -> 1L
            ']' -> 2L
            '}' -> 3L
            '>' -> 4L
            else -> 0L
        }

        object Valid : ValidationResult()
        data class Incomplete(val openParentheses: Deque<Char>) : ValidationResult() {
            val parenthesesToComplete: List<Char> = openParentheses.map { parenthesesDictionary[it]!! }
        }
        data class Corrupted(val wrongParentheses: Char) : ValidationResult()
    }
}