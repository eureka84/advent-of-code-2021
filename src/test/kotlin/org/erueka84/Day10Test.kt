package org.erueka84

import org.erueka84.Day10.ValidationResult.Incomplete
import org.erueka84.Day10.validate
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import kotlin.test.assertTrue

class Day10Test {

    @Test
    fun incomplete() {
        val validationResult = validate("{[(")
        assertTrue(validationResult.isIncomplete())

        val incomplete = validationResult as Incomplete
        assertThat(incomplete.parenthesesToComplete, equalTo(listOf(')', ']', '}')))
        assertThat(incomplete.incompleteLinesScore(), equalTo(38))
    }
}