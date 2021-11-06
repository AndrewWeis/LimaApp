package start.up.tracker.data.db.relations

import androidx.room.Entity

@Entity(primaryKeys = ["taskName", "categoryName"])
data class TaskCategoryCrossRef(
    val taskName: String,
    val categoryName: String
)