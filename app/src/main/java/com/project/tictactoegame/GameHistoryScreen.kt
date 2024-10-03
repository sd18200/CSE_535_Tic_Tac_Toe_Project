import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.tictactoegame.GameHistory
import com.project.tictactoegame.GameHistoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun GameHistoryScreen(
    gameHistoryDao: GameHistoryDao
) {
    val scope = rememberCoroutineScope()
    val gameHistories = remember { mutableStateOf(listOf<GameHistory>()) }

    LaunchedEffect(Unit) {
        scope.launch {
            gameHistoryDao.getAllGameHistory().collect { histories ->
                gameHistories.value = histories
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Game History", style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(gameHistories.value) { history ->
                Text("Winner: ${history.winner}, Date: ${history.date}, Difficulty: ${history.difficulty}",
                    style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
