package org.erueka84

import org.erueka84.Common.readLines
import java.util.*

object Day15 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readLines("/day15.input").toList()
        val grid = Grid.from(lines)
        println(part1(grid)) // 456
        println(part2(grid)) // 2831
    }

    private fun part1(grid: Grid): Int = grid.findShortestPath()

    private fun part2(grid: Grid): Int = grid.findShortestPath2()

    class Grid(private val array: Array<IntArray>) {

        private val rows: Int get() = array.size
        private val cols: Int get() = array.first().size
        private val origin = Node(0, 0)

        fun findShortestPath(): Int {
            val oneTileEnd = Node(rows - 1, cols - 1)
            return shortestPathFromOriginTo(oneTileEnd)
        }

        fun findShortestPath2(): Int {
            val fiveByFiveTilesEnd = Node((rows * 5) - 1, (cols * 5) - 1)
            return shortestPathFromOriginTo(fiveByFiveTilesEnd)
        }

        private fun shortestPathFromOriginTo(destination: Node): Int {

            val toBeEvaluated = PriorityQueue<PathNode>().apply {
                val initialNode = PathNode(origin, 0)
                add(initialNode)
            }
            val visited = mutableSetOf<Node>()

            while (toBeEvaluated.isNotEmpty()) {
                val currentNode = toBeEvaluated.poll()
                if (currentNode.point == destination) {
                    return currentNode.totalRisk
                }
                if (currentNode.point !in visited) {
                    visited.add(currentNode.point)
                    currentNode.point
                        .neighbors()
                        .filter { it isInRangeOf destination }
                        .forEach { toBeEvaluated.offer(PathNode(it, currentNode.totalRisk + getValueOf(it))) }
                }
            }
            error("No path to destination (which is really weird, right?)")
        }

        private fun getValueOf(point: Node): Int {
            val dx = point.x / rows
            val dy = point.y / cols
            val originalRisk = array[point.y % cols][point.x % rows]
            val newRisk = (originalRisk + dx + dy)
            return newRisk.takeIf { it < 10 } ?: (newRisk - 9)
        }

        private infix fun Node.isInRangeOf(destination: Node) =
            x in (0..destination.x) && y in (0..destination.y)

        companion object {
            fun from(input: List<String>): Grid {
                val array = input.map { row ->
                    row.map { risk ->
                        risk.digitToInt()
                    }.toIntArray()
                }.toTypedArray()
                return Grid(array)
            }
        }
    }


    data class Node(val x: Int, val y: Int) {
        fun neighbors(): List<Node> =
            listOf(
                Node(x, y + 1),
                Node(x, y - 1),
                Node(x + 1, y),
                Node(x - 1, y)
            )
    }

    class PathNode(val point: Node, val totalRisk: Int) : Comparable<PathNode> {
        override fun compareTo(other: PathNode): Int = this.totalRisk - other.totalRisk
    }
}