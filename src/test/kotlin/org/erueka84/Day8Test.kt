package org.erueka84

import org.erueka84.Day8.Display
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class Day8Test{

    @Test
    fun `parse display`() {
        val display =
            Display.from("be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe")

        assertThat(display.reading, equalTo(8394))

    }
}