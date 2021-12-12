package org.erueka84

import org.erueka84.Common.readLines
import java.util.*

object Day12 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day12.input")
        println(part1(input)) // 3779
        println(part2(input)) // 96988
    }

    private fun part1(input: Sequence<String>): Int {
        val graph = Graph.from(input)
        return graph.countPathsFromStartToEndV1()
    }

    private fun part2(input: Sequence<String>): Int {
        val graph = Graph.from(input)
        return graph.countPathsFromStartToEndV2()
    }

    data class Graph(val mapOfAdjacent: MutableMap<Node, Set<Node>> = mutableMapOf()) {

        fun countPathsFromStartToEndV1(): Int {
            val explorationQueue: Deque<Path> = LinkedList()
            explorationQueue.push(listOf("start"))
            val paths: MutableList<Path> = mutableListOf()
            while (explorationQueue.isNotEmpty()) {
                val path: Path = explorationQueue.pop()
                val lastNode: Node = path.last()
                val lastNodeAdjacentSet: Set<Node> = mapOfAdjacent[lastNode] ?: mutableSetOf()
                for (node in lastNodeAdjacentSet) {
                    if (node == "end") {
                        paths.add(path + node)
                    } else if (node.first().isUpperCase() || !path.contains(node)) {
                        explorationQueue.push(path + node)
                    }
                }
            }
            return paths.size
        }

        fun countPathsFromStartToEndV2(): Int {
            val explorationQueue: Deque<EnrichedPath> = LinkedList()
            explorationQueue.push(EnrichedPath(listOf("start")))
            val paths: MutableList<Path> = mutableListOf()
            while (explorationQueue.isNotEmpty()) {
                val enrichedPath = explorationQueue.pop()
                val (path, smallCave) = enrichedPath
                val lastNode: Node = path.last()
                val lastNodeAdjacentSet: Set<Node> = mapOfAdjacent[lastNode] ?: mutableSetOf()
                for (node in lastNodeAdjacentSet) {
                    if (node == "end") {
                        paths.add(path + node)
                    } else {
                        when {
                            node.first().isUpperCase() ->
                                explorationQueue.push(enrichedPath.copy(path = path + node))
                            !path.contains(node) ->
                                explorationQueue.push(enrichedPath.copy(path = path + node))
                            node != "start" && smallCave == null ->
                                explorationQueue.push(enrichedPath.copy(path = path + node, smallCave = node))
                        }
                    }
                }
            }
            return paths.size
        }

        data class EnrichedPath(val path: Path, val smallCave: Node? = null)

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