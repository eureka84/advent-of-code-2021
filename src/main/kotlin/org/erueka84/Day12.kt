package org.erueka84

import org.erueka84.Common.readLines
import java.util.*

object Day12 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day12.input")
        println(part1(input)) // 3779
    }

    private fun part1(input: Sequence<String>): Int {
        val graph = Graph.from(input)
        return graph.countPathsFromStartToEnd()
    }

    data class Graph(val mapOfAdjacent: MutableMap<Node, Set<Node>> = mutableMapOf()) {

        fun countPathsFromStartToEnd(): Int {
            val explorationQueue: Deque<Path> = LinkedList()
            explorationQueue.push(listOf("start"))
            val paths: MutableList<Path> = mutableListOf()
            while (explorationQueue.isNotEmpty()) {
                val path: Path = explorationQueue.pop()
                val last: Node = path.last()
                val set: Set<Node> = mapOfAdjacent[last]?: mutableSetOf()
                for (node in set) {
                    if (node == "end") {
                        paths.add(path)
                    } else if (node[0].isUpperCase() || !path.contains(node)) {
                        explorationQueue.push(path + node)
                    }
                }
            }
            return paths.size
        }

        private fun add(firstNode: Node, secondNode: Node) {
            addAdjacentToNode(firstNode, secondNode)
            addAdjacentToNode(secondNode, firstNode)
        }

        private fun addAdjacentToNode(node: Node, adjacent: Node) {
            val mutableSet = mapOfAdjacent[node] ?: mutableSetOf()
            mapOfAdjacent[node] = mutableSet + adjacent
        }

        companion object {
            fun from(input: Sequence<String>): Graph {
                val graph = Graph()
                input.forEach { line ->
                    line.split("-").let { (a, b) ->
                        graph.add(a, b)
                    }
                }
                return graph
            }
        }
    }

}

typealias Node = String
typealias Path = List<Node>