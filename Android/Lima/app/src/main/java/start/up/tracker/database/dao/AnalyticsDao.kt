package start.up.tracker.database.dao

import androidx.room.*
import start.up.tracker.entities.DayStat

@Dao
interface AnalyticsDao {

    @Query("SELECT * FROM daystat WHERE year =:year AND month =:month")
    suspend fun getStatMonth(year: Int, month: Int): List<DayStat>

    @Query("SELECT * FROM daystat WHERE year =:year")
    suspend fun getStatYear(year: Int): List<DayStat>

    @Query("SELECT * FROM daystat WHERE year =:year AND month =:month AND day =:day")
    suspend fun getStatDay(year: Int, month: Int, day: Int): DayStat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayStat(dayStat: DayStat)

    @Update
    suspend fun updateDayStat(dayStat: DayStat)

    @Delete
    suspend fun deleteDayStat(dayStat: DayStat)
}
