package org.erueka84

import org.erueka84.Common.readLines

object Day4 {

    @JvmStatic
    fun main(args: Array<String>) {
        println(solve())
    }

    private fun solve(): Pair<Int?, Int?> {
        val input = readLines("/day4.input").toList()
        val numbers = parseSequenceOfExtractedNumbers(input)
        val game = parseGame(input.drop(2))
        numbers.forEach { n ->
            game.draw(n)
        }
        return Pair(game.firstWinnerScore, game.lastWinnerScore)
    }

    private fun parseGame(inputs: List<String>): Game {
        val boards = inputs.windowed(5, 6).map(Board::from)
        return Game(boards)
    }

    private fun parseSequenceOfExtractedNumbers(input: List<String>) =
        input[0].split(",").map { it.toInt() }

    data class Game(val boards: List<Board>) {
        init {
            boards.forEach { b -> b.registerBingoCallBack(this::bingo) }
        }
        private var drawnNumbers = ArrayDeque<Int>()

        private var _firstWinnerScore: Int? = null
        private var _lastWinnerScore: Int? = null
        val firstWinnerScore get() = _firstWinnerScore
        val lastWinnerScore get() = _lastWinnerScore

        fun draw(n: Int) {
            drawnNumbers.add(n)
            boards.forEach { board -> board.onDraw(n) }
        }

        private fun bingo(board: Board) {
            val score = board.score() * drawnNumbers.last()
            if (_firstWinnerScore == null) {
                _firstWinnerScore = score
            }
            _lastWinnerScore = score
        }
    }

    data class Board(val rows: List<List<BoardNumber>>) {

        private val columns: Array<Array<BoardNumber>> = Array(5) { i -> Array(5) { j -> rows[j][i] } }
        private lateinit var bingoCallback: (Board) -> Unit
        private var gotBingo = false

        fun registerBingoCallBack(callback: (Board) -> Unit) {
            this.bingoCallback = callback
        }

        fun onDraw(n: Int) {
            if (!gotBingo) {
                rows.forEach { row -> row.forEach { boardNumber -> boardNumber.markDrawnIfIs(n) } }
                checkBingo()
            }
        }

        private fun checkBingo() {
            if (anyRowFullyDrawn() || anyColumnFullyDrawn()) {
                gotBingo = true
                bingoCallback(this)
            }
        }

        private fun anyColumnFullyDrawn() = columns.any { col -> col.all { number -> number.drawn } }

        private fun anyRowFullyDrawn() = rows.any { row -> row.all { number -> number.drawn } }

        fun score(): Int {
            return rows.flatten().filter { !it.drawn }.sumOf { it.value }
        }

        companion object {
            fun from(rawBoardRows: List<String>): Board {
                val rows = rawBoardRows.map {
                    val rawNumbers = it.trim().split("\\s+".toRegex())
                    rawNumbers.map(BoardNumber::from)
                }
                return Board(rows)
            }
        }
    }

    data class BoardNumber(val value: Int, var drawn: Boolean = false) {
        fun markDrawnIfIs(n: Int) {
            if (value == n) drawn = true
        }

        companion object {
            fun from(rawNumber: String): BoardNumber = BoardNumber(rawNumber.toInt())
        }
    }
}