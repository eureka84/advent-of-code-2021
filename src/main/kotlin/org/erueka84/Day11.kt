package org.erueka84

import org.erueka84.Common.readLines

typealias Step = Int
typealias Flashes = Int

object Day11 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day11.input")
        println(part1(input)) // 1793
        println(part2(input)) // 247
    }

    private fun part1(input: Sequence<String>): Int {
        val octopusGrid = OctopusGrid.from(input)
        (1..100).forEach { i -> octopusGrid.runStep(i) }
        return octopusGrid.flashes
    }

    private fun part2(input: Sequence<String>): Int? {
        val octopusGrid = OctopusGrid.from(input)
        val steps = generateSequence(1) { it + 1 }
        steps
            .takeWhile { !octopusGrid.haveAllOctopusFlashedAtOnce() }
            .forEach { step -> octopusGrid.runStep(step) }

        return octopusGrid.firstStepWithSynchronizedFlash()
    }

    data class OctopusGrid(val grid: List<List<Octopus>>) {
        init {
            grid.flatten().forEach { octopus -> octopus.registerOnFlashCallback(this::onFlash) }
        }
        private val _flashes = mutableMapOf<Step, Flashes>()
        val flashes: Flashes get() = _flashes.values.sum()

        fun runStep(step: Step) {
            grid.flatten().forEach { octopus -> octopus increaseEnergyOn step }
        }

        fun firstStepWithSynchronizedFlash(): Step? =
            _flashes.filter { (_, v) -> v == grid.numberOfPoints }.keys.minOrNull()

        fun haveAllOctopusFlashedAtOnce(): Boolean = firstStepWithSynchronizedFlash() != null

        private fun onFlash(flash: Flash) {
            updateFlashes(flash)
            increaseNeighboursEnergy(flash)
        }

        private fun increaseNeighboursEnergy(flash: Flash) {
            flash.let { (position, step) ->
                listOfAdjacentOf(position).forEach { octopus -> octopus increaseEnergyOn step }
            }
        }

        private fun updateFlashes(flash: Flash) {
            _flashes[flash.step] = (_flashes[flash.step] ?: 0) + 1
        }

        private fun listOfAdjacentOf(position: Position): List<Octopus> =
            pointsInASize3SquareCenteredIn(position)
                .filter { p -> exists(p) }
                .filter { p -> p != position }
                .map { (i, j) -> grid[i][j] }

        private fun pointsInASize3SquareCenteredIn(position: Position) =
            position.let { (x, y) ->
                val xRange = (x - 1)..(x + 1)
                val yRange = (y - 1)..(y + 1)
                xRange.flatMap { i -> yRange.map { j -> Position(i, j) } }
            }

        private fun exists(position: Position): Boolean =
            position.let { (x, y) -> (x in 0 until grid.rows) && (y in 0 until grid.cols) }

        override fun toString(): String =
            grid.joinToString(separator = "\n", postfix = "\n") { row ->
                row.joinToString(separator = "") { octopus -> octopus.energyLevel.toString() }
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

        fun registerOnFlashCallback(callback: (Flash) -> Unit) {
            this.onFlash = callback
        }

        infix fun increaseEnergyOn(step: Step) {
            if (!flashingSteps.contains(step)) {
                energyLevel += 1
                maybeFlashOn(step)
            }
        }

        private fun maybeFlashOn(step: Step) {
            if (energyLevel > 9) {
                flashingSteps.add(step)
                energyLevel = 0
                onFlash(Flash(position, step))
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