package com.project.tictactoegame

import androidx.lifecycle.LiveData
import com.project.tictactoegame.GameHistory
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
    @Insert
    fun insertGameHistory(gameHistory: GameHistory)

    @Query("SELECT * FROM game_history")
     fun getAllGameHistory(): Flow<List<GameHistory>>
}

