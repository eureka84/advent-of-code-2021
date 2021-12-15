package org.erueka84

import org.erueka84.Common.readLines
import java.util.*

object Day15 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readLines("/day15.input").toList()
        println(part1(lines)) // part 1
    }

    private fun part1(lines: List<String>): Int {
        val grid = Grid.from(lines)
        return grid.findShortestPath()
    }

    class Grid(private val array: Array<IntArray>) {

        private val rows: Int get() = array.size
        private val cols: Int get() = array.first().size
        private val origin = Node(0, 0)
        private val destination = Node(rows - 1, cols - 1)

        fun findShortestPath(): Int {
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
                        .filter { exists(it) }
                        .forEach { toBeEvaluated.offer(PathNode(it, currentNode.totalRisk + getValueOf(it))) }
                }
            }
            error("No path to destination (which is really weird, right?)")
        }

        private fun getValueOf(point: Node): Int = array[point.x][point.y]

        private fun exists(it: Node) =
            it.x in (0 until rows) && it.y in (0 until cols)

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