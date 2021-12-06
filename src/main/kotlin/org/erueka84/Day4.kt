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
        val game = BingoGame.from(input.drop(2))
        numbers.forEach { n ->
            game.draw(n)
        }
        return Pair(game.firstWinnerScore, game.lastWinnerScore)
    }

    private fun parseSequenceOfDrawnNumbers(s: String) =
        s.split(",").map { it.toInt() }

    data class BingoGame(private val boards: List<Board>) {
        init {
            boards.forEach { b -> b.registerBingoCallBack(this::bingo) }
        }
        private val drawnNumbers = ArrayDeque<Int>()

        private var _firstWinnerScore: Int? = null
        private var _lastWinnerScore: Int? = null
        val firstWinnerScore get() = _firstWinnerScore
        val lastWinnerScore get() = _lastWinnerScore

        fun draw(n: Int) {
            drawnNumbers.add(n)
            boards.forEach { board -> board.onDrawn(n) }
        }

        private fun bingo(board: Board) {
            val score = board.score() * drawnNumbers.last()
            if (_firstWinnerScore == null) {
                _firstWinnerScore = score
            }
            _lastWinnerScore = score
        }

        companion object {
            fun from(inputs: List<String>): BingoGame {
                val boards = inputs.windowed(5, 6).map(Board::from)
                return BingoGame(boards)
            }
        }
    }

    data class Board(private val rows: List<List<BoardNumber>>) {

        private val columns: Array<Array<BoardNumber>> = Array(5) { i -> Array(5) { j -> rows[j][i] } }
        private lateinit var callBingo: (Board) -> Unit
        private var gotBingo = false

        fun registerBingoCallBack(callback: (Board) -> Unit) {
            this.callBingo = callback
        }

        fun onDrawn(n: Int) {
            if (!gotBingo) {
                rows.forEach { row -> row.forEach { boardNumber -> boardNumber.onDrawn(n) } }
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
        fun onDrawn(n: Int) {
            if (value == n) drawn = true
        }

        companion object {
            fun from(rawNumber: String): BoardNumber = BoardNumber(rawNumber.toInt())
        }
    }
}