package com.project.tictactoegame

import GameHistoryScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.project.tictactoegame.ui.theme.TicTacToeGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            "game_database"
        ).build()
        setContent {
            TicTacToeGameTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TicTacToeApp(db)
                }
            }
        }
    }
}

@Composable
fun TicTacToeApp(database: GameDatabase) {
    val navController = rememberNavController()
    var selectedDifficulty by remember { mutableStateOf("Easy") }
    NavHost(navController = navController, startDestination = "game") {
        composable("game") {
            GameScreen(
                onSettingsClick = { navController.navigate("settings") },
                onHistoryClick = { navController.navigate("gameHistory") },
                difficulty = selectedDifficulty,
                database = database
            )
        }
        composable("settings") {
            SettingsScreen(
                onDifficultyChange = { difficulty ->
                    selectedDifficulty = difficulty
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("gameHistory") {
            GameHistoryScreen(
                gameHistoryDao = database.gameHistoryDao()
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val dummyDatabase = Room.databaseBuilder(
        LocalContext.current,
        GameDatabase::class.java,
        "game_database"
    ).build()
    TicTacToeGameTheme {
        TicTacToeApp(dummyDatabase)
    }
}
