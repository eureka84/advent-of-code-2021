package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.MathLib.triangular
import kotlin.math.max
import kotlin.math.min

object Day17 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = readLines("/day17.input").first()
        val targetArea = TargetArea.parse(input)
        println(part1(targetArea)) // 2850
        println(part2(targetArea)) // 1117
    }

    private fun part1(targetArea1: TargetArea): Int = targetArea1.y0.triangular()

    private fun part2(targetArea: TargetArea): Int = targetArea.acceptableInitialVelocities().size

    data class TargetArea(val x0: Int, val x1: Int, val y0: Int, val y1: Int) {

        fun acceptableInitialVelocities(): List<Velocity> {
            val dxMin = generateSequence(1) { it + 1 }.first { it.triangular() >= x0 }
            val dxMax = x1
            val dxRange = dxMin..dxMax
            val dyRange = y0 until -y0
            return dxRange.flatMap { dx -> dyRange.map { dy -> Velocity(dx, dy) } }.filter { trajectoryAcceptable(it) }
        }

        private fun trajectoryAcceptable(v: Velocity): Boolean {
            val trajectory = trajectory(v)
            val isAcceptable = trajectory endsIn this
            if (isAcceptable)
                println(trajectory.last())
            return isAcceptable
        }

        private fun trajectory(v: Velocity): List<Point> {
            val result = mutableListOf<Point>()
            var vx = v.dx
            var vy = v.dy
            var x = 0
            var y = 0
            var point = Point(x, y)
            while (y >= y0 && !(this contains point)) {
                x += vx
                y += vy
                point = Point(x, y)
                vx += if (vx == 0) 0 else if (vx > 0) -1 else 1
                vy--
                result.add(point)
            }
            return result
        }

        private infix fun List<Point>.endsIn(targetArea: TargetArea) = !isEmpty() && targetArea contains last()

        infix fun contains(p: Point): Boolean {
            return p.x in x0..x1 && p.y in y0..y1
        }

        companion object {
            fun parse(input: String): TargetArea {
                val number = "(-?\\d+)"
                val twoDots = "\\.{2}"
                val regex = "target area: x=$number$twoDots$number, y=$number$twoDots$number".toRegex()
                val (_, x0, x1, y0, y1) = regex.matchEntire(input)!!.groupValues
                val x01: Int = x0.toInt()
                val x11: Int = x1.toInt()
                val y01 = y0.toInt()
                val y11 =  y1.toInt()
                val minX = min(x01, x11)
                val minY = min(y01, y11)
                val maxX = max(x01, x11)
                val maxY = max(y01, y11)
                return TargetArea(minX, maxX, minY, maxY)
            }
        }
    }

    data class Velocity(val dx: Int, val dy: Int)

    data class Point(val x: Int, val y: Int) {
        override fun toString(): String = "($x, $y)"
    }

}
