package org.erueka84

import org.erueka84.Common.readLines

object Day13 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day13.input").toList()

        val (grid, foldActions) = parse(input)

        val gridAfterOneFold = part1(grid, foldActions)
        println(gridAfterOneFold.size) // 770

        val gridAfterAllFolds = part2(grid, foldActions)
        println(gridAfterAllFolds.size) // 102
        println(gridAfterAllFolds) // EPUELPBR
    }

    private fun parse(input: List<String>): Pair<Grid, List<FoldLine>> {
        val separator = input.indexOf("")
        val rawPoints = input.subList(0, separator)
        val rawFoldLines = input.subList(separator + 1, input.size)

        val grid = Grid.from(rawPoints)
        val foldActions = rawFoldLines.map { rawLine -> FoldLine.from(rawLine) }

        return Pair(grid, foldActions)
    }

    private fun part1(grid: Grid, foldActions: List<FoldLine>): Grid =
        grid foldAlong foldActions.first()

    private fun part2(grid: Grid, foldLines: List<FoldLine>): Grid =
        foldLines.fold(grid) { currGrid, line -> currGrid foldAlong line }

    data class Grid(val points: Set<Point>) {
        val size: Int = points.size

        infix fun foldAlong(line: FoldLine): Grid {
            val newPoints = points.map { it foldAlong line }.toSet()
            return Grid(newPoints)
        }

        override fun toString(): String {
            val cols = points.maxOf { it.x }
            val rows = points.maxOf { it.y }

            val array = Array(rows + 1) { Array(cols + 1) { '.' } }
            points.forEach { (x, y) -> array[y][x] = '#' }

            return array.joinToString("\n") {
                it.joinToString(separator = " ")
            }
        }

        companion object {
            fun from(input: List<String>): Grid {
                val set = input.map { Point.from(it) }.toSet()
                return Grid(set)
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        infix fun foldAlong(line: FoldLine): Point = when (line) {
            is VerticalLine ->
                if (x > line.x) {
                    val dx = x - line.x
                    copy(x = line.x - dx)
                } else {
                    this
                }
            is HorizontalLine ->
                if (y > line.y) {
                    val dy = y - line.y
                    copy(y = line.y - dy)
                } else {
                    this
                }
        }

        companion object {
            fun from(line: String): Point =
                line.split(",")
                    .map { it.toInt() }
                    .let { (x, y) -> Point(x, y) }
        }
    }

    sealed class FoldLine {
        companion object {
            private val foldXRegex = "x=(\\d+)".toRegex()
            private val foldYRegex = "y=(\\d+)".toRegex()

            fun from(rawLine: String): FoldLine =
                if (foldXRegex.find(rawLine) != null)
                    VerticalLine(rawLine extractValueFrom foldXRegex)
                else
                    HorizontalLine(rawLine extractValueFrom foldYRegex)

            private infix fun String.extractValueFrom(regex: Regex) =
                regex.find(this)!!.groupValues[1].toInt()
        }
    }

    data class HorizontalLine(val y: Int) : FoldLine()
    data class VerticalLine(val x: Int) : FoldLine()

}