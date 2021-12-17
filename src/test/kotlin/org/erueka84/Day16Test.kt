package org.erueka84

import org.erueka84.Day16.next
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test

class Day16Test {

    @Test
    fun `next n char`() {
        val test = "A string"
        val iterator = test.iterator()

        assertThat(iterator.next(2), equalTo("A "))
        assertThat(iterator.next(3), equalTo("str"))

    }
}

