package start.up.tracker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table for active analytics
 */
@Entity
data class TaskAnalytics (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val taskId: Int? = null,
    val title: String? = null,
    val completed: Boolean = false,
    val completedInTime: Boolean = false,
    val date: Long? = null,

    val categoryId: Int? = null,
    val categoryName: String? = null,
)