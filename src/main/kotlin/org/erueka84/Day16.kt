package org.erueka84

import org.erueka84.Common.readLines

object Day16 {

    @JvmStatic
    fun main(args: Array<String>) {
        val packet = readPacket()
        println(part1(packet)) // 949
        println(part2(packet)) // 1114600142730
    }

    private fun part2(packet: Packet): Long = packet.evaluate()

    private fun part1(packet: Packet): Int = packet.versionSums

    private fun readPacket(): Packet {
        val input = readLines("/day16.input").first()
        val binaryTransmission = input.map(::mapHexToBinary).joinToString(separator = "")
        return parse(binaryTransmission.iterator())
    }

    fun mapHexToBinary(hexChar: Char) =
        hexChar.digitToInt(16).toString(2).padStart(4, '0')

    private fun parse(iterator: CharIterator): Packet =
        iterator.next(3).binaryToInt().let { version ->
            when (val type = iterator.next(3).binaryToInt()) {
                4 -> parseLiteral(iterator, version)
                else -> parseOperator(iterator, version, type)
            }
        }

    private fun parseLiteral(iterator: CharIterator, version: Int): Literal {
        val bytes = iterate()
            .takeWhile { iterator.next() != '0' }
            .map { iterator.next(4) }
            .toList() + iterator.next(4)
        return Literal(version, bytes)
    }

    private fun parseOperator(iterator: CharIterator, version: Int, type: Int): Operator =
        when (iterator.next(1).binaryToInt()) {
            1 -> {
                val numberOfPackets = iterator.next(11).binaryToInt()
                val packets = iterate().map { parse(iterator) }.take(numberOfPackets)
                Operator(version, type, packets.toList())
            }
            else -> {
                val payloadLength = iterator.next(15).binaryToInt()
                val subPackets = iterator.next(payloadLength).iterator()
                val packets = iterate().takeWhile { subPackets.hasNext() }.map { parse(subPackets) }
                Operator(version, type, packets.toList())
            }
        }

    sealed class Packet {
        abstract val versionSums: Int
        abstract val version: Int
        abstract val type: Int

        abstract fun evaluate(): Long
    }

    data class Literal(override val version: Int, val payload: List<String>) : Packet() {
        override val versionSums: Int
            get() = version
        override val type: Int
            get() = 4

        override fun evaluate(): Long {
            return payload.joinToString(separator = "").toLong(2)
        }
    }

    data class Operator(override val version: Int, override val type: Int, val packets: List<Packet>) : Packet() {
        override val versionSums: Int
            get() = version + packets.sumOf { it.versionSums }

        override fun evaluate(): Long =
            when (type) {
                0 -> packets.sumOf { it.evaluate() }
                1 -> packets.productOf { it.evaluate() }
                2 -> packets.minOf { it.evaluate() }
                3 -> packets.maxOf { it.evaluate() }
                5 -> if (packets[0].evaluate() > packets[1].evaluate()) 1 else 0
                6 -> if (packets[0].evaluate() < packets[1].evaluate()) 1 else 0
                else -> if (packets[0].evaluate() == packets[1].evaluate()) 1 else 0
            }
    }

    private fun iterate(): Sequence<Int> = generateSequence { 1 }

    fun <T> Iterable<T>.productOf(f: (T) -> Long): Long = this.map(f).reduce { acc, el -> acc * el }

    fun CharIterator.next(n: Int): String = (1..n).map { next() }.toCharArray().let { String(it) }

    private fun String.binaryToInt() = this.toInt(2)

}