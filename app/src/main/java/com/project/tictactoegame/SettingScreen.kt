package com.project.tictactoegame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onDifficultyChange: (String) -> Unit, onBack: () -> Unit) {
    var selectedDifficulty by remember { mutableStateOf("Easy") }
    val difficultyLevels = listOf("Easy", "Medium", "Hard")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Select Difficulty:", style = MaterialTheme.typography.titleMedium)
        difficultyLevels.forEach { level ->
            Button(
                onClick = {
                    selectedDifficulty = level
                    onDifficultyChange(level)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedDifficulty == level) Color.Green else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = level)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text(text = "Back to Game")
        }
    }
}
