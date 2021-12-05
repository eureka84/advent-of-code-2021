package org.erueka84

import org.erueka84.Common.readLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day5 {

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
                .flatMap { it.getAllContainedPoints().toList() }
                .groupBy { it }
                .filter { (_, v) -> v.size >= 2 }
                .keys
        }

        companion object {
            fun from(segments: List<Segment>): Grid {
                return Grid(segments)
            }
        }
    }

    private fun parseSegments(input: Sequence<String>): List<Segment> {
        val regex = "(\\d+,\\d+)\\s->\\s(\\d+,\\d+)".toRegex()
        return input.toList().map { line ->
            val (_, p0, p1) = regex.matchEntire(line)?.groupValues!!
            Segment.from(Point.from(p0), Point.from(p1))
        }
    }

    data class Segment(val p0: Point, val p1: Point) {

        val isVertical: Boolean = p0.x == p1.x
        val isHorizontal: Boolean = p0.y == p1.y

        fun getAllContainedPoints(): Set<Point> {
            val (numerator, denominator) = p0.inclinationTO(p1)

            return when {
                numerator == 0 ->
                    (p0.x..p1.x).map { Point(it, p0.y) }.toSet()
                numerator > 0 && denominator == 0 ->
                    (p0.y..p1.y).map { Point(p0.x, it) }.toSet()
                numerator < 0 && denominator == 0 ->
                    (p0.y downTo p1.y).map { Point(p0.x, it) }.toSet()
                numerator > 0 ->
                    (p0.y..p1.y step numerator).zip((p0.x..p1.x step denominator)).map { (y, x) -> Point(x, y) }.toSet()
                numerator < 0 ->
                    (p0.y downTo p1.y step (-numerator)).zip((p0.x..p1.x step denominator))
                        .map { (y, x) -> Point(x, y) }.toSet()
                else -> setOf(p0, p1)
            }
        }

        fun covers(point: Point): Boolean =
            p0.inclinationTO(p1) == p0.inclinationTO(point) && point.liesBetween(p0, p1)

        companion object {
            fun from(a: Point, b: Point): Segment =
                if (a.x < b.x)
                    Segment(a, b)
                else
                    Segment(b, a)
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun inclinationTO(other: Point): Fraction = Fraction.from(other.y - this.y, other.x - this.x)
        fun liesBetween(p0: Point, p1: Point): Boolean {
            val minX = min(p0.x, p1.x)
            val minY = min(p0.y, p1.y)
            val maxX = max(p0.x, p1.x)
            val maxY = max(p0.y, p1.y)
            return x in (minX..maxX) && y in (minY..maxY)
        }

        companion object {
            fun from(s: String): Point = s.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) }
        }
    }

    data class Fraction(val numerator: Int, val denominator: Int) {
        companion object {
            fun from(numerator: Int, denominator: Int): Fraction =
                if (numerator == 0 || denominator == 0) {
                    Fraction(numerator, denominator)
                } else {
                    val gcd = gcd(numerator, denominator)
                    Fraction(numerator / gcd, denominator / gcd)
                }
        }
    }


    private fun gcd(a: Int, b: Int): Int {
        var x = abs(a)
        var y = abs(b)
        while (x != y) {
            if (x > y) {
                x -= y
            } else {
                y -= x
            }
        }
        return x
    }

}