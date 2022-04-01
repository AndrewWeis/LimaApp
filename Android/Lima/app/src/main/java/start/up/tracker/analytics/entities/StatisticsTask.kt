package start.up.tracker.analytics.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StatisticsTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val status: TaskStatus,
    val projectId: Int,
    val creationTime: Long,
    val expectedCompletionTime: Long?,
    val completionTime: Long,
)

enum class TaskStatus {
    IN_PROGRESS, COMPLETED_IN_TIME, COMPLETED_LATE, COMPLETED
}
