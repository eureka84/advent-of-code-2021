package org.erueka84

import org.erueka84.Day13.HorizontalLine
import org.erueka84.Day13.Point
import org.erueka84.Day13.VerticalLine
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test

class Day13Test {

    @Test
    fun `fold vertical`() {
        val point = Point(3, 4)
        val action = VerticalLine(2)

        val expected = Point(1, 4)

        assertThat(point.foldAlong(action), equalTo(expected))
    }

    @Test
    fun `fold vertical left of the axis`() {
        val point = Point(3, 4)
        val action = VerticalLine(5)

        assertThat(point.foldAlong(action), equalTo(point))
    }


    @Test
    fun `fold horizontal`() {
        val point = Point(3, 4)
        val action = HorizontalLine(2)

        val expected = Point(3, 0)

        assertThat(point.foldAlong(action), equalTo(expected))
    }

    @Test
    fun `fold horizontal below y`() {
        val point = Point(3, 4)
        val action = HorizontalLine(6)

        assertThat(point.foldAlong(action), equalTo(point))
    }
}