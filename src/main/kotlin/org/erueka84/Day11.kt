package org.erueka84

import org.erueka84.Common.readLines

object Day11 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day11.input")
        println(part1(input)) // 1793
    }

    private fun part1(input: Sequence<String>): Int {
        val octopusGrid = OctopusGrid.from(input)
        for (i in (1..100)) {
            octopusGrid.onStep(i)
        }
        return octopusGrid.flashes
    }

    data class OctopusGrid(val grid: List<List<Octopus>>) {
        init {
            grid.flatten().forEach { octopus -> octopus.registerOnFlash(this::onFlash) }
        }

        val flashes: Int get() = grid.flatten().sumOf { octopus -> octopus.flashes }

        private val rows: Int = grid.size
        private val cols: Int = grid.first().size

        fun onStep(i: Int) {
            grid.flatten().forEach { octopus -> octopus.onStep(i) }
        }

        private fun onFlash(flash: Flash) {
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
            position.let { (x, y) -> (x in 0 until rows) && (y in 0 until cols) }

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
        private val _flashes = mutableListOf<Int>()
        val flashes: Int get() = _flashes.size

        private lateinit var onFlash: (Flash) -> Unit

        fun registerOnFlash(callback: (Flash) -> Unit) {
            this.onFlash = callback
        }

        fun onStep(i: Int) {
            if (!_flashes.contains(i)) {
                energyLevel += 1
                maybeFlash(i)
            }
        }

        private fun maybeFlash(i: Int) {
            if (energyLevel > 9) {
                _flashes.add(i)
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

}