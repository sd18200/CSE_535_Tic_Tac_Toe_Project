package com.project.tictactoegame

import com.project.tictactoegame.GameHistory
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*




@Composable
fun GameScreen(
    onSettingsClick: () -> Unit,
    onHistoryClick: () -> Unit,  // Add a new callback parameter for navigating to the history screen
    difficulty: String,
    database: GameDatabase // Accept the database instance
) {
    val context = LocalContext.current
    var board by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameOver by remember { mutableStateOf(false) }
    var gameResultMessage by remember { mutableStateOf("") }
    val ticTacToeAI = TicTacToeAI(aiPlayer = "O", humanPlayer = "X")
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Tic-Tac-Toe", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (!gameOver) "Current Player: $currentPlayer" else gameResultMessage,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Button(
                        onClick = {
                            if (board[i][j].isEmpty() && !gameOver && currentPlayer == "X") {
                                board[i][j] = currentPlayer
                                if (checkWinner(board, currentPlayer)) {
                                    gameResultMessage = "Congratulations! Player $currentPlayer wins!"
                                    gameOver = true
                                    saveGameHistory(database, currentPlayer, difficulty, scope) {
                                        Toast.makeText(context, "Game history saved for Player $currentPlayer", Toast.LENGTH_SHORT).show()
                                    }
                                } else if (isBoardFull(board)) {
                                    gameResultMessage = "It's a draw!"
                                    gameOver = true
                                    saveGameHistory(database, "Draw", difficulty, scope) {
                                        Toast.makeText(context, "Game history saved for a draw", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    currentPlayer = "O"
                                    scope.launch(Dispatchers.Main) {
                                        makeAiMove(board, ticTacToeAI, difficulty)
                                        if (checkWinner(board, "O")) {
                                            gameResultMessage = "Player O (AI) wins!"
                                            gameOver = true
                                            saveGameHistory(database, "O", difficulty, scope) {
                                                Toast.makeText(context, "Game history saved for Player O", Toast.LENGTH_SHORT).show()
                                            }
                                        } else if (isBoardFull(board)) {
                                            gameResultMessage = "It's a draw!"
                                            gameOver = true
                                            saveGameHistory(database, "Draw", difficulty, scope) {
                                                Toast.makeText(context, "Game history saved for a draw", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            currentPlayer = "X"
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(80.dp)
                    ) {
                        Text(text = board[i][j])
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            board = Array(3) { Array(3) { "" } }
            currentPlayer = "X"
            gameOver = false
            gameResultMessage = ""
        }) {
            Text(text = "Reset Game")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSettingsClick) {
            Text(text = "Settings")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onHistoryClick) {
            Text(text = "Game History")
        }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(date)
}

fun saveGameHistory(
    database: GameDatabase,
    winner: String,
    difficulty: String,
    scope: CoroutineScope,
    onHistorySaved: () -> Unit
) {
    val dateString = formatDate(Date())
    val gameHistory = GameHistory(
        date = dateString,
        winner = winner,
        difficulty = difficulty
    )

    scope.launch(Dispatchers.IO) {
        database.gameHistoryDao().insertGameHistory(gameHistory)
        withContext(Dispatchers.Main) {
            onHistorySaved()
        }
    }
}

fun makeAiMove(board: Array<Array<String>>, ticTacToeAI: TicTacToeAI, difficulty: String) {
    val bestMove = ticTacToeAI.findBestMove(board, difficulty)
    if (bestMove != null) {
        board[bestMove.first][bestMove.second] = "O"
    }
}

fun checkWinner(board: Array<Array<String>>, player: String): Boolean {
    for (i in 0..2) {
        if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
            (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
            return true
        }
    }
    if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
        (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
        return true
    }
    return false
}

fun isBoardFull(board: Array<Array<String>>): Boolean {
    return board.all { row -> row.all { cell -> cell.isNotEmpty() } }
}

