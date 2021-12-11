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
        assertTrue(validationResult is Incomplete)

        assertThat(validationResult.parenthesesToComplete, equalTo(listOf(')', ']', '}')))
        assertThat(validationResult.incompleteLinesScore(), equalTo(38))
    }
}