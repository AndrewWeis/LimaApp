package start.up.tracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DayStat (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val completedTasks: Int
)