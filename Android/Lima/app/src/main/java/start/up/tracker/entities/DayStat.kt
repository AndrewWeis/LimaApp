package start.up.tracker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DayStat (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val day: Int,
    val month: Int,
    val year: Int,
    val allTasks: Int = 1,
    val completedTasks: Int = 1,
)