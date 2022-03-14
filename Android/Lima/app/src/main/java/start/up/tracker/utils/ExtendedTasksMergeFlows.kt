package start.up.tracker.utils

import start.up.tracker.entities.Category
import start.up.tracker.entities.Task

object ExtendedTasksMergeFlows {

    fun mergeFlowsForExtendedTask(
        hideCompleted: Boolean,
        tasks: List<Task>,
        categories: List<Category>
    ): List<Task> {
        val tasksWithCategoryData: MutableList<Task> = mutableListOf()

        tasks
            .filter { task ->
                task.completed != hideCompleted || !task.completed
            }
            .forEach { task ->
                categories.forEach { category ->
                    if (category.id == task.categoryId) {
                        tasksWithCategoryData.add(
                            task.copy(categoryName = category.name, categoryColor = category.color)
                        )
                    }
                }
            }

        return tasksWithCategoryData
    }
}
