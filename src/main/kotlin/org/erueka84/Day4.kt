package org.erueka84

import org.erueka84.Common.readLines

object Day4 {

    @JvmStatic
    fun main(args: Array<String>) {
        println(solve()) // (16674, 7075)
    }

    private fun solve(): Pair<Int?, Int?> {
        val input = readLines("/day4.input").toList()
        val numbers = parseSequenceOfDrawnNumbers(input[0])
        val game = parseGame(input.drop(2))
        numbers.forEach { n ->
            game.draw(n)
        }
        return Pair(game.firstWinnerScore, game.lastWinnerScore)
    }

    private fun parseSequenceOfDrawnNumbers(s: String) =
        s.split(",").map { it.toInt() }

    private fun parseGame(inputs: List<String>): BingoGame {
        val cards = inputs.windowed(5, 6).map(Card::from)
        return BingoGame(cards)
    }

    data class BingoGame(private val cards: List<Card>) {
        init {
            cards.forEach { b -> b.registerBingoCallBack(this::bingo) }
        }
        private var drawnNumbers = ArrayDeque<Int>()

        private var _firstWinnerScore: Int? = null
        private var _lastWinnerScore: Int? = null
        val firstWinnerScore get() = _firstWinnerScore
        val lastWinnerScore get() = _lastWinnerScore

        fun draw(n: Int) {
            drawnNumbers.add(n)
            cards.forEach { board -> board.onDrawn(n) }
        }

        private fun bingo(card: Card) {
            val score = card.score() * drawnNumbers.last()
            if (_firstWinnerScore == null) {
                _firstWinnerScore = score
            }
            _lastWinnerScore = score
        }
    }

    data class Card(private val rows: List<List<CardNumber>>) {

        private val columns: Array<Array<CardNumber>> = Array(5) { i -> Array(5) { j -> rows[j][i] } }
        private lateinit var callBingo: (Card) -> Unit
        private var gotBingo = false

        fun registerBingoCallBack(callback: (Card) -> Unit) {
            this.callBingo = callback
        }

        fun onDrawn(n: Int) {
            if (!gotBingo) {
                rows.forEach { row -> row.forEach { cardNumber -> cardNumber.onDrawn(n) } }
                if (anyRowFullyDrawn() || anyColumnFullyDrawn()) {
                    gotBingo = true
                    callBingo(this)
                }
            }
        }

        private fun anyColumnFullyDrawn() = columns.any { col -> col.all { number -> number.drawn } }

        private fun anyRowFullyDrawn() = rows.any { row -> row.all { number -> number.drawn } }

        fun score(): Int = rows.flatten().filter { !it.drawn }.sumOf { it.value }

        companion object {
            fun from(rawCardRows: List<String>): Card {
                val rows = rawCardRows.map {
                    val rawNumbers = it.trim().split("\\s+".toRegex())
                    rawNumbers.map(CardNumber::from)
                }
                return Card(rows)
            }
        }
    }

    data class CardNumber(val value: Int, var drawn: Boolean = false) {
        fun onDrawn(n: Int) {
            if (value == n) drawn = true
        }

        companion object {
            fun from(rawNumber: String): CardNumber = CardNumber(rawNumber.toInt())
        }
    }
}