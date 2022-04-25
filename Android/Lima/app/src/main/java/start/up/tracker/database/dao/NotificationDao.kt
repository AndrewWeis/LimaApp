package start.up.tracker.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import start.up.tracker.entities.Notification

interface NotificationDao {
    @Query("SELECT * FROM notification_table")
    fun getNotifications(): Flow<List<Notification>>

    @Query("SELECT * FROM notification_table WHERE id =:id")
    fun getNotificationById(id: Int): Flow<Notification>

    @Update
    suspend fun updateNotification(notification: Notification)

    @Delete
    suspend fun deleteProject(notification: Notification)
}