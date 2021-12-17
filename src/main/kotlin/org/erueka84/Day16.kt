package org.erueka84

import org.erueka84.Common.readLines

object Day16 {

    @JvmStatic
    fun main(args: Array<String>) {
        val packet = readPacket()
        println(part1(packet)) // 949
    }

    private fun part1(packet: Packet): Int {
        return packet.versionSums
    }

    private fun readPacket(): Packet {
        val input = readLines("/day16.input").first()
        val binaryTransmission = input.map(::mapHexToBinary).joinToString(separator = "")
        return parse(binaryTransmission.iterator())
    }

    fun mapHexToBinary(hexChar: Char) = hexChar.digitToInt(16).toString(2).padStart(4, '0')

    private fun parse(iterator: CharIterator): Packet {
        val version = iterator.next(3).binaryToInt()
        return when (val type = iterator.next(3).binaryToInt()) {
            4 -> parseLiteral(iterator, version)
            else -> parseOperator(iterator, version, type)
        }
    }

    private fun parseLiteral(iterator: CharIterator, version: Int): Literal {
        val bytes = mutableListOf<String>()
        while (iterator.next() == '1') {
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