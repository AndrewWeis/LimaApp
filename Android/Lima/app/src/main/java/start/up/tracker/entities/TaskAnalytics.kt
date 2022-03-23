package start.up.tracker.entities

import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table for active analytics
 */
@Entity(tableName = "taskAnalytics_table")
data class TaskAnalytics (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val taskId: Int? = null,
    val title: String? = null,
    val deleted: Boolean = false,
    val completed: Boolean = false,
    val completedInTime: Boolean = false,
    val day: Long? = null,
    val date: Long? = null,

    val categoryId: Int? = null,
    val categoryName: String? = null,
)