package org.erueka84

import org.erueka84.Common.readLines
import org.erueka84.MathLib.Fraction
import kotlin.math.max
import kotlin.math.min

object Day5 {

    private val SEGMENT_LINE_PATTERN = "(\\d+,\\d+)\\s->\\s(\\d+,\\d+)".toRegex()

    @JvmStatic
    fun main(args: Array<String>) {
        println(part1().findHotSpots().size) // 6267
        println(part2().findHotSpots().size) // 20196
    }

    private fun part1(): Grid {
        val input = readLines("/day5.input")
        val segments = parseSegments(input).filter { it.isHorizontal || it.isVertical }
        return Grid.from(segments)
    }

    private fun part2(): Grid {
        val input = readLines("/day5.input")
        val segments = parseSegments(input)
        return Grid.from(segments)
    }

    data class Grid(val segments: List<Segment>) {

        fun findHotSpots(): Set<Point> {
            return segments
                .flatMap { it.getAllContainedPoints() }
                .groupingBy { it }
                .eachCount()
                .filter { (_, v) -> v >= 2 }
                .keys
        }

        companion object {
            fun from(segments: List<Segment>): Grid {
                return Grid(segments)
            }
        }
    }

    private fun parseSegments(input: Sequence<String>): List<Segment> =
        input.toList().map { line ->
            val (_, p0, p1) = SEGMENT_LINE_PATTERN.matchEntire(line)?.groupValues!!
            Segment.from(Point.from(p0), Point.from(p1))
        }


    sealed class Segment {
        abstract val isVertical: Boolean
        abstract val isHorizontal: Boolean
        abstract fun getAllContainedPoints(): List<Point>

        companion object {
            fun from(p0: Point, p1: Point): Segment = when {
                p0.y == p1.y -> HorizontalSegment.from(p0.x, p1.x, p0.y)
                p0.x == p1.x -> VerticalSegment.from(p0.x, p0.y, p1.y)
                else -> DiagonalSegment.from(p0, p1)
            }
        }
    }

    data class HorizontalSegment internal constructor(val x1: Int, val x2: Int, val y: Int) : Segment() {
        override val isHorizontal: Boolean = true
        override val isVertical: Boolean = false
        override fun getAllContainedPoints(): List<Point> = (x1..x2).map { Point(it, y) }

        companion object {
            fun from(x1: Int, x2: Int, y: Int) = HorizontalSegment(min(x1, x2), max(x1, x2), y)
        }
    }

    data class VerticalSegment internal constructor(val x: Int, val y1: Int, val y2: Int) : Segment() {
        override val isHorizontal: Boolean = false
        override val isVertical: Boolean = true
        override fun getAllContainedPoints(): List<Point> = (y1..y2).map { Point(x, it) }

        companion object {
            fun from(x: Int, y1: Int, y2: Int) = VerticalSegment(x, min(y1, y2), max(y1, y2))
        }
    }

    data class DiagonalSegment internal constructor(val p0: Point, val p1: Point) : Segment() {
        override val isHorizontal: Boolean = false
        override val isVertical: Boolean = false
        override fun getAllContainedPoints(): List<Point> {
            val (numerator, denominator) = p0.inclinationTo(p1)
            val yRange = if (numerator > 0) (p0.y..p1.y step numerator) else (p0.y downTo p1.y step (-numerator))
            val xRange = p0.x..p1.x step denominator
            return xRange.zip(yRange).map { (x, y) -> Point(x, y) }
        }

        companion object {
            fun from(p0: Point, p1: Point): DiagonalSegment =
                if (p0.x < p1.x) DiagonalSegment(p0, p1) else DiagonalSegment(p1, p0)
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun inclinationTo(other: Point): Fraction =
            Fraction.from(other.y - this.y, other.x - this.x)

        companion object {
            fun from(s: String): Point = s.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) }
        }
    }

}