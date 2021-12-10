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

        assertThat((validationResult as Incomplete).parenthesesToComplete, equalTo(listOf(')', ']', '}')))
    }
}