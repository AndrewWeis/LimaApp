package start.up.tracker.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Notification
import start.up.tracker.entities.Project

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification_table")
    fun getNotifications(): Flow<List<Notification>>

    @Query("SELECT * FROM notification_table WHERE id =:id")
    fun getNotificationById(id: Int): Flow<Notification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification): Long

    @Update
    suspend fun updateNotification(notification: Notification)

    @Delete
    suspend fun deleteNotification(notification: Notification)
}