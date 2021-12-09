package org.erueka84

import org.erueka84.Common.readLines

object Day09 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day09.input")
        println(part1(input)) // 550
    }

    private fun part1(input: Sequence<String>): Int = Grid.from(input).riskLevel()

    data class Grid(private val map: List<List<Point>>) {

        private val rows: Int get() = map.size
        private val cols: Int get() = map[0].size

        init {
            (0 until rows).forEach { i ->
                (0 until cols).forEach { j ->
                    if (pointIsLowerThanAllHisNeighbours(i, j)){
                        map[i][j].isLow = true
                    }
                }
            }
        }

        fun riskLevel(): Int = map.flatten().filter { it.isLow }.sumOf { it.height +1 }

        private fun pointIsLowerThanAllHisNeighbours(i: Int, j: Int) =
            neighbours(i, j).all { map[i][j].height < it.height }

        private fun neighbours(i: Int, j: Int): List<Point> {
            val list = mutableListOf<Point>()
            if (i - 1 >= 0) list.add(map[i - 1][j])
            if (i + 1 < rows) list.add(map[i + 1][j])
            if (j - 1 >= 0) list.add(map[i][j - 1])
            if (j + 1 < cols) list.add(map[i][j + 1])
            return list
        }

        companion object {
            fun from(input: Sequence<String>): Grid =
                input.toList().map { it.map { c -> Point(c.digitToInt()) } }.let { Grid(it) }
        }
    }

    data class Point(val height: Int, var isLow: Boolean = false)

}