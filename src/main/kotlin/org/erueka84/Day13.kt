package org.erueka84

import org.erueka84.Common.readLines

object Day13 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day13.input")

        val rawPoints = input.takeWhile { it.isNotEmpty() }
        val rawFolActions = input.dropWhile { it.isNotEmpty() }.drop(1).toList()
        val grid = Grid.from(rawPoints)
        val foldActions = FoldAction.from(rawFolActions)

        val gridAfterOneFold = part1(grid, foldActions)
        println(gridAfterOneFold.size) // 770

        val gridAfterAllFolds = part2(grid, foldActions)
        println(gridAfterAllFolds) // EPUELPBR
    }

    private fun part1(
        grid: Grid,
        foldActions: List<FoldAction>
    ): Grid = grid.applyFoldAction(foldActions.first())

    private fun part2(grid: Grid, foldActions: List<FoldAction>): Grid {
        return foldActions.fold(grid) { currGrid, currAction -> currGrid.applyFoldAction(currAction) }
    }

    data class Grid(val points: Set<Point>) {
        val size: Int = points.size

        fun applyFoldAction(foldAction: FoldAction): Grid {
            val foldedPoints = points.map { it.applyFold(foldAction) }.toSet()
            return Grid(foldedPoints)
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
            fun from(input: Sequence<String>): Grid {
                val set = input.map { Point.from(it) }.toSet()
                return Grid(set)
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun applyFold(fold: FoldAction): Point {
            return when (fold) {
                is Vertical -> if (x > fold.x) copy(x = (fold.x - (x - fold.x))) else this
                is Horizontal -> if (y > fold.y) copy(y = (fold.y - (y - fold.y))) else this
            }
        }

        companion object {
            fun from(line: String): Point {
                return line.split(",").let { (rawX, rawY) -> Point(rawX.toInt(), rawY.toInt()) }
            }
        }
    }

    sealed class FoldAction {
        companion object {
            fun from(input: List<String>): List<FoldAction> {
                val foldXRegex = "x=(\\d+)".toRegex()
                val foldYRegex = "y=(\\d+)".toRegex()
                return input.map {
                    val find = foldXRegex.find(it)
                    if (find != null)
                        Vertical(find.groupValues[1].toInt())
                    else
                        Horizontal(foldYRegex.find(it)!!.groupValues[1].toInt())
                }
            }
        }
    }

    data class Horizontal(val y: Int) : FoldAction()
    data class Vertical(val x: Int) : FoldAction()

}