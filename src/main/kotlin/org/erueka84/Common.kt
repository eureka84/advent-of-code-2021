@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package org.erueka84

import kotlin.math.abs

object Common {
    fun readLines(file: String): Sequence<String> =
        object {}::class.java.getResource(file).readText().lineSequence()
}

object MathLib {
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

    tailrec fun gcd(a: Int, b: Int): Int {
        val x = abs(a)
        val y = abs(b)

        if (x == y) return x
        if (x > y)
            return gcd(x - y, y)
        else
            return gcd(x, y - x)
    }

    fun Int.triangular() = sumOfAllNaturalsUpTo(this)

    private fun sumOfAllNaturalsUpTo(n: Int): Int = n * (n + 1) / 2

    fun List<Int>.mean(): Int = this.sum() / this.size

    fun List<Int>.median(): Int =
        this.sorted()
            .let { sorted ->
                val size = sorted.size
                if (size % 2 == 0) {
                    (sorted[size / 2] + sorted[(size / 2) - 1]) / 2
                } else {
                    sorted[size / 2]
                }
            }
}