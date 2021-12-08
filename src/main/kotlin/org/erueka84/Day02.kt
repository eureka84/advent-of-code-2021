package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.Day02.Command.Companion.parseCommand
import org.erueka84.Day02.Direction.*
import org.erueka84.Day02.Direction.Companion.parse

object Day02 {

    enum class Direction {
        UP, DOWN, FORWARD;

        companion object {
            fun parse(s: String): Direction? {
                return values().find { d -> d.name.lowercase() == s }
            }
        }
    }

    data class Command(val direction: Direction, val steps: Int) {
        companion object {
            fun parseCommand(line: String): Command? =
                line.split(" ").let { (rawDirection, rawSteps) ->
                    parse(rawDirection)?.let { direction -> Command(direction, rawSteps.toInt()) }
                }
        }
    }

    data class SubMarine(val horizontalPosition: Int = 0, val depth: Int = 0) {
        infix fun execute(command: Command): SubMarine =
            when (command.direction) {
                UP -> copy(depth = depth - command.steps)
                DOWN -> copy(depth = depth + command.steps)
                FORWARD -> copy(horizontalPosition = horizontalPosition + command.steps)
            }

        fun score(): Int = depth * horizontalPosition
    }

    data class SubMarine2(val horizontalPosition: Int = 0, val depth: Int = 0, val aim: Int = 0) {
        infix fun execute(command: Command): SubMarine2 =
            when (command.direction) {
                UP -> copy(aim = aim - command.steps)
                DOWN -> copy(aim = aim + command.steps)
                FORWARD -> copy(
                    horizontalPosition = horizontalPosition + command.steps,
                    depth = depth + (aim * command.steps)
                )
            }

        fun score(): Int = depth * horizontalPosition
    }

    private fun part1(): SubMarine =
        readLines("/day02.input")
            .map { parseCommand(it)!! }
            .fold(SubMarine()) { subMarine, command -> subMarine execute command }

    private fun part2(): SubMarine2 =
        readLines("/day02.input")
            .map { parseCommand(it)!! }
            .fold(SubMarine2()) { subMarine, command -> subMarine execute command }

    @JvmStatic
    fun main(args: Array<String>) {
        println(part1().score())
        println(part2().score())
    }
}
