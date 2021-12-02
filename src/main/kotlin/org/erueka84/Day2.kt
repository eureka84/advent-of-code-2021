package org.erueka84

import org.erueka84.Command.Companion.parseCommand
import org.erueka84.Common.readLines
import org.erueka84.Direction.*

enum class Direction(val value: String) {
    UP("up"), DOWN("down"), FORWARD("forward");

    companion object {
        fun from(s: String): Direction? {
            return values().find { d -> d.value == s }
        }
    }
}

data class Command(val direction: Direction, val steps: Int) {
    companion object {
        fun parseCommand(line: String): Command? {
            val split: List<String> = line.split(" ")
            return Direction.from(split[0])?.let { direction ->
                Command(direction, split[1].toInt())
            }
        }
    }
}

data class SubMarine(val horizontalPosition: Int = 0, val depth: Int = 0) {
    fun execute(command: Command): SubMarine =
        when (command.direction) {
            UP -> this.copy(depth = this.depth - command.steps)
            DOWN -> this.copy(depth = this.depth + command.steps)
            FORWARD -> this.copy(horizontalPosition = this.horizontalPosition + command.steps)
        }
}

object Day2 {
    fun puzzle1(): SubMarine =
        readLines("/day2.input")
            .map { parseCommand(it)!! }
            .fold(SubMarine()) { sub, command -> sub.execute(command) }

    @JvmStatic
    fun main(args: Array<String>) {
        println(puzzle1())
    }
}
