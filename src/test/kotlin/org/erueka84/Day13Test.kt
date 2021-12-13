package org.erueka84

import org.erueka84.Day13.Horizontal
import org.erueka84.Day13.Point
import org.erueka84.Day13.Vertical
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test

class Day13Test {

    @Test
    fun `fold vertical`() {
        val point = Point(3, 4)
        val action = Vertical(2)

        val expected = Point(1, 4)

        assertThat(point.applyFold(action), equalTo(expected))
    }

    @Test
    fun `fold vertical out of grid`() {
        val point = Point(3, 4)
        val action = Vertical(2)

        val expected = Point(1, 4)

        assertThat(point.applyFold(action), equalTo(expected))
    }

    @Test
    fun `fold horizontal`() {
        val point = Point(3, 4)
        val action = Horizontal(2)

        val expected = Point(3, 0)

        assertThat(point.applyFold(action), equalTo(expected))
    }
}