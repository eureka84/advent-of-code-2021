package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.Day10.ValidationResult.*
import java.util.*

object Day10 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day10.input")
        println(part1(input)) // 344193
        println(part2(input))
    }

    private fun part1(input: Sequence<String>): Long =
        input.map { validate(it) }
            .fold(0L) { acc, curr ->
                when (curr) {
                    is Valid -> acc
                    is Incomplete -> acc
                    is Corrupted -> acc + when (curr.wrongParenthesis) {
                        ')' -> 3L
                        ']' -> 57L
                        '}' -> 1197L
                        '>' -> 25137L
                        else -> 0L
                    }
                }
            }

    private fun part2(input: Sequence<String>): Long {
        TODO("Not yet implemented")
    }

    private val parenthesesDictionary = mapOf('{' to '}', '[' to ']', '(' to ')', '<' to '>')

    private fun validate(s: String): ValidationResult {
        if (s.isEmpty()) return Valid

        val openParentheses: Deque<Char> = LinkedList()
        s.toCharArray().forEach { c ->
            if (parenthesesDictionary.containsKey(c)) {
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
            return Incomplete
    }

    sealed class ValidationResult {
        object Valid : ValidationResult()
        object Incomplete : ValidationResult()
        data class Corrupted(val wrongParenthesis: Char) : ValidationResult()
    }

}