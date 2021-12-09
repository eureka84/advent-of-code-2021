package org.erueka84

import org.erueka84.Common.readLines
import java.util.*

object Day09 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day09.input")
        println(part1(input)) // 550
        println(part2(input)) // 1100682
    }

    private fun part1(input: Sequence<String>): Int = Grid.from(input).riskLevel()
    private fun part2(input: Sequence<String>): Int = Grid.from(input).basinsAreas()

    data class Grid(private val map: List<List<Point>>) {

        private val rows: Int get() = map.size
        private val cols: Int get() = map[0].size

        init {
            (0 until rows).forEach { i ->
                (0 until cols).forEach { j ->
                    if (pointIsLowerThanAllHisNeighbours(i, j)) {
                        map[i][j].isLow = true
                    }
                }
            }
        }

        fun riskLevel(): Int = map.flatten().filter { it.isLow }.sumOf { it.height + 1 }

        private fun pointIsLowerThanAllHisNeighbours(i: Int, j: Int) =
            map.listOfAdjacentOf(map[i][j]).all { map[i][j].height < it.height }

        fun basinsAreas(): Int {
            val visited = mutableMapOf<Point, Boolean>()
            val basinsAreas = mutableListOf<Int>()

            map.indices.forEach { i ->
                map[i].indices.forEach { j ->
                    val node = map[i][j]
                    if (!visited.containsKey(node) && map.isNotOnTopOfAHill(node)) {
                        val basin = findBasin(map, visited, node)
                        basinsAreas.add(basin.size)
                    }
                }
            }

            basinsAreas.sortDescending()

            return basinsAreas.take(3).reduce { acc, curr -> acc * curr }
        }

        private fun findBasin(map: List<List<Point>>, visited: MutableMap<Point, Boolean>, node: Point): List<Point> {
            visited[node] = true
            val deque: Queue<Point> = LinkedList()
            deque.add(node)
            val basin = mutableListOf<Point>()
            basin.add(node)
            while (deque.isNotEmpty()) {
                map.listOfAdjacentOf(deque.poll()).forEach { adj ->
                    if (map.isNotOnTopOfAHill(adj) && !visited.containsKey(adj)) {
                        visited[adj] = true
                        deque.add(adj)
                        basin.add(adj)
                    }
                }
            }
            return basin
        }

        companion object {
            fun from(input: Sequence<String>): Grid =
                input.toList().mapIndexed { i, line ->
                    line.mapIndexed { j, c ->
                        Point(i, j, c.digitToInt())
                    }
                }.let { Grid(it) }
        }
    }

    data class Point(val x: Int, val y: Int, val height: Int, var isLow: Boolean = false)

    fun List<List<Point>>.listOfAdjacentOf(point: Point): List<Point> {
        val contiguousPositions = listOf(Pair(-1, 0), Pair(0, 1), Pair(0, -1), Pair(1, 0))
        return contiguousPositions
            .map { (x, y) -> Pair(point.x + x, point.y + y) }
            .filter { (i, j) -> this.exists(i, j) }
            .map { (i, j) -> this[i][j] }
    }

    fun List<List<Point>>.exists(i: Int, j: Int): Boolean =
        (this.size > i && i >= 0) && (this[i].size > j && j >= 0)

    fun List<List<Point>>.isNotOnTopOfAHill(node: Point): Boolean =
        this.exists(node.x, node.y) && this[node.x][node.y].height != 9

}

