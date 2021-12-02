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
            val steps = split[1].toInt()
            val maybeDirection = Direction.from(split[0])
            return maybeDirection?.let { direction ->
                Command(direction, steps)
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

    fun score(): Int = depth * horizontalPosition
}

data class SubMarine2(val horizontalPosition: Int = 0, val depth: Int = 0, val aim: Int =0) {
    fun execute(command: Command): SubMarine2 =
        when (command.direction) {
            UP -> this.copy(aim = this.aim - command.steps)
            DOWN -> this.copy(aim = this.aim + command.steps)
            FORWARD -> this.copy(
                horizontalPosition = this.horizontalPosition + command.steps,
                depth = this.depth + (this.aim * command.steps)
            )
        }

    fun score(): Int = depth * horizontalPosition
}

object Day2 {
    private fun part1(): SubMarine =
        readLines("/day2.input")
            .map { parseCommand(it)!! }
            .fold(SubMarine()) { sub, command -> sub.execute(command) }

    private fun part2(): SubMarine2 =
        readLines("/day2.input")
            .map { parseCommand(it)!! }
            .fold(SubMarine2()) { sub, command -> sub.execute(command) }

    @JvmStatic
    fun main(args: Array<String>) {
        println(part1().score())
        println(part2().score())
    }
}
