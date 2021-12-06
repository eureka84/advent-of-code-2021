package org.erueka84

import org.erueka84.Common.readLines

object Day6Part1 {

    @JvmStatic
    fun main(args: Array<String>) {
        println(simulate(80)) // 359999
    }

    private fun simulate(days: Int): Int {
        val input = readLines("/day6.input")
        val sea = Sea.parse(input.first())
        repeat(days) {
            sea.onDayPassed()
        }
        return sea.fishPopulation
    }

    data class Sea(private val fish: MutableList<LanternFish> = mutableListOf()) {
        val fishPopulation: Int get() = fish.size

        fun add(lanternFish: LanternFish) {
            fish.add(lanternFish)
        }

        fun onDayPassed() {
            fish.addAll(fish.mapNotNull { f -> f.onDayPassed() })
        }

        companion object {
            fun parse(input: String): Sea {
                val sea = Sea()
                input.split(",").map { LanternFish(it.toInt()) }.forEach { f -> sea.add(f) }
                return sea
            }
        }
    }

    data class LanternFish(private var internalTimer: Int = 8) {

        fun onDayPassed(): LanternFish? =
            if (internalTimer == 0) {
                internalTimer = 6
                LanternFish()
            } else {
                internalTimer -= 1
                null
            }
    }

}

object Day6Part2 {
    @JvmStatic
    fun main(args: Array<String>) {
        println(part2()) // 1631647919273
    }

    private fun part2(): Long {
        val lines = readLines("/day6.input")
        val sea = initializeSea(lines)

        repeat(256) {
            sea.onDayPassed()
        }

        return sea.fishPopulation
    }

    private fun initializeSea(lines: Sequence<String>): Sea {
        val sea = Sea()
        lines.first().split(",").forEach { time -> sea.addFishWithTimeToProcreate(time.toInt()) }
        return sea
    }

    class Sea {
        private var lanternFishGenerations: Array<Long> = Array(9) { 0 }

        val fishPopulation get() = lanternFishGenerations.sum()

        fun addFishWithTimeToProcreate(time: Int) {
            lanternFishGenerations[time]++
        }

        fun onDayPassed() {
            val newGeneration = Array<Long>(9) { 0 }
            (0..7).forEach { generation ->
                newGeneration[generation] = lanternFishGenerations[generation+1]
            }
            newGeneration[8] = lanternFishGenerations[0]
            newGeneration[6] += lanternFishGenerations[0]

            lanternFishGenerations = newGeneration
        }

    }
}