package org.erueka84

import org.erueka84.Common.readLines

object Day11 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day11.input")
        println(part1(input)) // 1793
        println(part2(input)) // 247
    }

    private fun part1(input: Sequence<String>): Int {
        val octopusGrid = OctopusGrid.from(input)
        (1..100).forEach { i -> octopusGrid.onStep(i) }
        return octopusGrid.flashes
    }

    private fun part2(input: Sequence<String>): Int? {
        val octopusGrid = OctopusGrid.from(input)
        var i = 1
        while (!octopusGrid.haveAllOctopusFlashedAtOnce()) {
            octopusGrid.onStep(i++)
        }
        return octopusGrid.firstSynchronizedFlash()
    }

    data class OctopusGrid(val grid: List<List<Octopus>>) {
        init {
            grid.flatten().forEach { octopus -> octopus.registerOnFlash(this::onFlash) }
        }

        private val _flashes = mutableMapOf<Int, Int>()

        val flashes: Int get() = _flashes.values.sum()

        fun onStep(i: Int) {
            grid.flatten().forEach { octopus -> octopus.onStep(i) }
        }

        fun firstSynchronizedFlash(): Int? =
            _flashes.filter { (_, v) -> v == grid.numberOfPoints }.keys.minOrNull()

        fun haveAllOctopusFlashedAtOnce(): Boolean = firstSynchronizedFlash() != null

        private fun onFlash(flash: Flash) {
            _flashes[flash.step] = (_flashes[flash.step] ?: 0) + 1
            listOfAdjacentOf(flash.position).forEach { octopus -> octopus.onStep(flash.step) }
        }

        private fun listOfAdjacentOf(position: Position): List<Octopus> =
            position.let { (x, y) ->
                ((x - 1)..(x + 1))
                    .flatMap { i -> ((y - 1)..(y + 1)).map { j -> Position(i, j) } }
                    .filter { p -> exists(p) }
                    .filter { p -> p != position }
                    .map { (i, j) -> grid[i][j] }
            }

        private fun exists(position: Position): Boolean =
            position.let { (x, y) -> (x in 0 until grid.rows) && (y in 0 until grid.cols) }

        override fun toString(): String {
            return grid.joinToString(separator = "\n", postfix = "\n") { row ->
                row.joinToString(separator = "") { octopus -> octopus.energyLevel.toString() }
            }
        }

        companion object {
            fun from(input: Sequence<String>): OctopusGrid {
                val list = input.mapIndexed { i, line ->
                    line.mapIndexed { j, c ->
                        Octopus.from(i, j, c)
                    }
                }.toList()
                return OctopusGrid(list)
            }
        }
    }

    data class Octopus(val position: Position, var energyLevel: Int) {
        private val flashingSteps = mutableListOf<Int>()

        private lateinit var onFlash: (Flash) -> Unit

        fun registerOnFlash(callback: (Flash) -> Unit) {
            this.onFlash = callback
        }

        fun onStep(step: Int) {
            if (!flashingSteps.contains(step)) {
                energyLevel += 1
                maybeFlash(step)
            }
        }

        private fun maybeFlash(i: Int) {
            if (energyLevel > 9) {
                flashingSteps.add(i)
                energyLevel = 0
                onFlash(Flash(position, i))
            }
        }

        companion object {
            fun from(i: Int, j: Int, char: Char): Octopus = Octopus(Position(i, j), char.digitToInt())
        }
    }

    data class Flash(val position: Position, val step: Int)

    data class Position(val x: Int, val y: Int) {
        override fun toString(): String {
            return "($x, $y)"
        }
    }

    private val List<List<Octopus>>.rows: Int get() = this.size
    private val List<List<Octopus>>.cols: Int get() = this.first().size
    private val List<List<Octopus>>.numberOfPoints: Int get() = this.rows * this.cols

}