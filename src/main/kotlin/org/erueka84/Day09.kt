package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.Day09.Point
import java.util.*

typealias BasinMap = List<List<Point>>

object Day09 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day09.input")
        val grid = Grid.from(input)
        println(part1(grid)) // 550
        println(part2(grid)) // 1100682
    }

    private fun part1(grid: Grid): Int = grid.riskLevel()
    private fun part2(grid: Grid): Int = grid.basinsAreas()

    data class Grid(private val map: BasinMap) {

        private val rows: Int get() = map.size
        private val cols: Int get() = map[0].size

        init {
            (0 until rows).forEach { i ->
                (0 until cols).forEach { j ->
                    val point = map[i][j]
                    val neighbours = map.listOfAdjacentOf(point)
                    if (neighbours.all { point.height < it.height }) {
                        point.isLow = true
                    }
                }
            }
        }

        fun riskLevel(): Int = map.flatten().filter { it.isLow }.sumOf { it.height + 1 }

        fun basinsAreas(): Int {
            val visited = mutableMapOf<Point, Boolean>()
            val basinsAreas = mutableListOf<Int>()

            map.indices.forEach { i ->
                map[i].indices.forEach { j ->
                    val node = map[i][j]
                    if (!visited.containsKey(node) && map.isNotOnTopOfAHill(node)) {
                        val basinArea = exploreBasinArea(map, visited, node)
                        basinsAreas.add(basinArea.size)
                    }
                }
            }

            basinsAreas.sortDescending()

            return basinsAreas.take(3).reduce { acc, curr -> acc * curr }
        }

        private fun exploreBasinArea(map: BasinMap, visited: MutableMap<Point, Boolean>, node: Point): List<Point> {
            visited[node] = true
            val basinAreaPoints = mutableListOf<Point>()
            basinAreaPoints.add(node)
            val explorationQueue: Queue<Point> = LinkedList()
            explorationQueue.add(node)
            while (explorationQueue.isNotEmpty()) {
                map.listOfAdjacentOf(explorationQueue.poll()).forEach { adj ->
                    if (map.isNotOnTopOfAHill(adj) && !visited.containsKey(adj)) {
                        visited[adj] = true
                        explorationQueue.add(adj)
                        basinAreaPoints.add(adj)
                    }
                }
            }
            return basinAreaPoints
        }

        companion object {
            fun from(input: Sequence<String>): Grid =
                input.toList().mapIndexed { i, mapRow ->
                    mapRow.mapIndexed { j, rawHeight ->
                        Point(Position(i, j), rawHeight.digitToInt())
                    }
                }.let { Grid(it) }
        }
    }

    data class Point(val position: Position, val height: Int, var isLow: Boolean = false) {
        val x get() = position.x
        val y get() = position.y
    }

    data class Position(val x: Int, val y: Int)

    private fun BasinMap.listOfAdjacentOf(point: Point): List<Point> {
        val contiguousPositions = listOf(
            Position(point.x - 1, point.y),
            Position(point.x, point.y + 1),
            Position(point.x, point.y - 1),
            Position(point.x + 1, point.y)
        )
        return contiguousPositions
            .filter { p -> this.containsPosition(p) }
            .map { (i, j) -> this[i][j] }
    }

    private fun BasinMap.containsPosition(p: Position): Boolean =
        (this.size > p.x && p.x >= 0) && (this[p.x].size > p.y && p.y >= 0)

    private fun BasinMap.isNotOnTopOfAHill(point: Point): Boolean =
        this.containsPosition(point.position) && point.height != 9

}

