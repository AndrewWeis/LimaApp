package start.up.tracker.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["taskId", "categoryId"], tableName = "cross_ref")
data class TaskCategoryCrossRef(
    val taskId: Int,
    val categoryId: Int
)