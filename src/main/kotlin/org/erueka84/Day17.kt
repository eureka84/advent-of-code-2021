package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.MathLib.triangular

object Day17 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day17.input").first()
        println(part1(input))
    }

    private fun part1(input: String): Int {
        val targetArea = TargetArea.parse(input)
        return targetArea.y0.triangular()
    }

    data class TargetArea(val x0: Int, val x1:Int, val y0: Int, val y1:Int) {
        companion object {
            fun parse(input: String): TargetArea {
                val number = "(-?\\d+)"
                val twoDots = "\\.{2}"
                val regex = "target area: x=$number$twoDots$number, y=$number$twoDots$number".toRegex()
                val (_, x0, x1, y0, y1) = regex.matchEntire(input)!!.groupValues
                return TargetArea(x0.toInt(), x1.toInt(), y0.toInt(), y1.toInt())
            }
        }
    }

}