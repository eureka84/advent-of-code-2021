package org.erueka84

import org.erueka84.Common.readLines

object Day16 {

    @JvmStatic
    fun main(args: Array<String>) {
        println(part1())
    }

    private fun part1(): Int {
        val input = readLines("/day16.input").first()
        val binaryTransmission =
            input
                .map { hexChar -> mapHexToBinary(hexChar) }
                .joinToString(separator = "")

        val packet= parse(binaryTransmission.iterator())
        return packet.versionSums
    }

    private fun mapHexToBinary(hexChar: Char) = when (hexChar) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> ""
    }

    private fun parse(iterator: CharIterator): Packet {
        val version = iterator.next(3).binaryToInt()
        return when (val type = iterator.next(3).binaryToInt()) {
            4 -> parseLiteral(iterator, version)
            else -> parseOperator(iterator, version, type)
        }
    }

    private fun parseLiteral(iterator: CharIterator, version: Int): Literal {
        val bytes = mutableListOf<String>()
        while (iterator.next() == '1'){
            bytes.add(iterator.next(4))
        }
        bytes.add(iterator.next(4))
        return Literal(version, bytes)
    }

    private fun parseOperator(iterator: CharIterator, version: Int, type: Int): Operator =
        when (iterator.next(1).binaryToInt()) {
            1 -> {
                val numberOfPackets = iterator.next(11).binaryToInt()
                val packets =
                    (1..numberOfPackets).fold(listOf<Packet>()) { packets, _ ->
                        val packet = parse(iterator)
                        packets + packet
                    }
                Operator(version, type, packets)
            }
            else -> {
                val payloadLength = iterator.next(15).binaryToInt()
                val packets = mutableListOf<Packet>()
                val subPackets = iterator.next(payloadLength).iterator()
                while (subPackets.hasNext()) {
                    val packet = parse(subPackets)
                    packets.add(packet)
                }
                Operator(version, type, packets)
            }
        }

    sealed class Packet {
        abstract val versionSums: Int
        abstract val version: Int
        abstract val type: Int
        abstract val length: Int
    }

    data class Literal(override val version: Int, val payload: List<String>) : Packet() {
        override val versionSums: Int
            get() = version
        override val type: Int
            get() = 4
        override val length: Int
            get() = HEADERS_LENGTH + 5 * payload.size
    }

    data class Operator(override val version: Int, override val type: Int, val packets: List<Packet>) : Packet() {
        override val versionSums: Int
            get() = version + packets.sumOf { it.versionSums }
        override val length: Int
            get() = HEADERS_LENGTH + packets.sumOf { it.length }
    }

    fun CharIterator.next(n: Int): String = (1..n).map { next() }.toCharArray().let { String(it) }

    private fun String.binaryToInt() = this.toInt(2)

    private const val HEADERS_LENGTH = 6

}