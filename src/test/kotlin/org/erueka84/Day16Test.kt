package org.erueka84

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.erueka84.Day16.mapHexToBinary
import org.erueka84.Day16.next
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class Day16Test {

    @Test
    fun `next n char`() {
        val test = "A string"
        val iterator = test.iterator()

        assertThat(iterator.next(2), equalTo("A "))
        assertThat(iterator.next(3), equalTo("str"))
    }

    @Test
    @Parameters(
        "0, 0000",
        "1, 0001",
        "2, 0010",
        "3, 0011",
        "4, 0100",
        "5, 0101",
        "6, 0110",
        "7, 0111",
        "8, 1000",
        "9, 1001",
        "A, 1010",
        "B, 1011",
        "C, 1100",
        "D, 1101",
        "E, 1110",
        "F, 1111"
    )
    fun `hex to bin`(hex: Char, binary: String) {
        assertThat(mapHexToBinary(hex), equalTo(binary))
    }
}

