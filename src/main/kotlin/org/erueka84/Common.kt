@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package org.erueka84

object Common {
    fun readLines(file: String) = object {}::class.java.getResource(file).readText().lineSequence()
}