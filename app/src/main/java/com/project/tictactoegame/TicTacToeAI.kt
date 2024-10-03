package com.project.tictactoegame

import android.util.Log
import kotlin.random.Random

class TicTacToeAI(private val aiPlayer: String, private val humanPlayer: String) {
    fun findBestMove(board: Array<Array<String>>, difficulty: String): Pair<Int, Int>? {
        return when (difficulty) {
            "Easy" -> {
                Log.d("TicTacToeAI", "AI is using random moves (Easy mode)")
                findRandomMove(board)
            }
            "Medium" -> {
                if (Random.nextBoolean()) {
                    Log.d("TicTacToeAI", "AI is using random moves (Medium mode)")
                    findRandomMove(board)
                } else {
                    Log.d("TicTacToeAI", "AI is using Minimax (Medium mode)")
                    findOptimalMove(board)
                }
            }
            "Hard" -> {
                Log.d("TicTacToeAI", "AI is using Minimax (Hard mode)")
                findOptimalMove(board)
            }
            else -> {
                Log.d("TicTacToeAI", "AI is using Minimax (Default mode)")
                findOptimalMove(board)
            }
        }
    }

    private fun findRandomMove(board: Array<Array<String>>): Pair<Int, Int>? {
        val availableMoves = mutableListOf<Pair<Int, Int>>()

        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].isEmpty()) {
                    availableMoves.add(Pair(i, j))
                }
            }
        }
        return if (availableMoves.isNotEmpty()) {
            availableMoves.random()
        } else null
    }

    private fun findOptimalMove(board: Array<Array<String>>): Pair<Int, Int>? {
        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<Int, Int>? = null
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = aiPlayer
                    val score = minimax(board, 0, false, Int.MIN_VALUE, Int.MAX_VALUE)
                    board[i][j] = ""

                    if (score > bestScore) {
                        bestScore = score
                        bestMove = Pair(i, j)
                    }
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: Array<Array<String>>, depth: Int, isMaximizing: Boolean, alpha: Int, beta: Int): Int {
        Log.d("Minimax", "Evaluating board at depth $depth with maximizing: $isMaximizing")
        val score = evaluateBoard(board)

        if (score != 0) return score
        if (!isMovesLeft(board)) return 0

        var alphaVar = alpha
        var betaVar = beta

        if (isMaximizing) {
            var best = Int.MIN_VALUE
            for (i in board.indices) {
                for (j in board[i].indices) {
                    if (board[i][j].isEmpty()) {
                        board[i][j] = aiPlayer
                        best = maxOf(best, minimax(board, depth + 1, false, alphaVar, betaVar))
                        board[i][j] = ""
                        alphaVar = maxOf(alphaVar, best)
                        if (betaVar <= alphaVar) break
                    }
                }
            }
            return best
        } else {
            var best = Int.MAX_VALUE
            for (i in board.indices) {
                for (j in board[i].indices) {
                    if (board[i][j].isEmpty()) {
                        board[i][j] = humanPlayer
                        best = minOf(best, minimax(board, depth + 1, true, alphaVar, betaVar))
                        board[i][j] = ""
                        betaVar = minOf(betaVar, best)
                        if (betaVar <= alphaVar) break
                    }
                }
            }
            return best
        }
    }

    private fun evaluateBoard(board: Array<Array<String>>): Int {
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == aiPlayer) return 10
                if (board[i][0] == humanPlayer) return -10
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == aiPlayer) return 10
                if (board[0][i] == humanPlayer) return -10
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == aiPlayer) return 10
            if (board[0][0] == humanPlayer) return -10
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == aiPlayer) return 10
            if (board[0][2] == humanPlayer) return -10
        }
        return 0
    }

    private fun isMovesLeft(board: Array<Array<String>>): Boolean {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].isEmpty()) return true
            }
        }
        return false
    }
}
