package start.up.tracker.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Technique

@Dao
interface TechniquesDao {

    @Query("SELECT * FROM technique_table")
    fun getTechniques(): Flow<List<Technique>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTechniques(vararg technique: Technique)

    @Update
    suspend fun updateTechnique(technique: Technique)
}
