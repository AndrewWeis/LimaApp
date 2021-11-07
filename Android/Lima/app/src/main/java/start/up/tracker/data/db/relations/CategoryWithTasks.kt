package start.up.tracker.data.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import start.up.tracker.data.db.models.Category
import start.up.tracker.data.db.models.Task

data class CategoryWithTasks (
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryName",
        entityColumn = "taskName",
        associateBy = Junction(TaskCategoryCrossRef::class)
    )
    val tasks: List<Task>
)