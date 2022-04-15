package start.up.tracker.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Technique

@Dao
interface TechniquesDao {

    @Query("SELECT id FROM technique_table WHERE isEnabled == 1")
    suspend fun getActiveTechniquesIds(): List<Int>

    @Query("SELECT id FROM technique_table WHERE isEnabled == 1")
    fun getActivePrinciplesIds(): Flow<List<Int>>

    @Query("SELECT * FROM technique_table")
    fun getTechniques(): Flow<List<Technique>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTechniques(vararg technique: Technique)

    @Update
    suspend fun updateTechnique(technique: Technique)
}
