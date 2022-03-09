package start.up.tracker.mvvm.view_models.today

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import start.up.tracker.data.analytics.Analytics
import start.up.tracker.data.database.PreferencesManager
import start.up.tracker.data.database.dao.CategoriesDao
import start.up.tracker.data.database.dao.TaskDao
import start.up.tracker.data.database.dao.TodayTasksDao
import start.up.tracker.data.entities.Category
import start.up.tracker.data.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodayTasksViewModel @Inject constructor(
    taskDao: TaskDao,
    preferencesManager: PreferencesManager,
    analytics: Analytics,
    todayTasksDao: TodayTasksDao,
    categoriesDao: CategoriesDao
) : BaseTasksOperationsViewModel(taskDao, preferencesManager, analytics) {

    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentDate: String = formatter.format(Date())

    private val todayTasksFlow = todayTasksDao.getTodayTasks(currentDate)
    private val categoriesFlow = categoriesDao.getCategories()

    private val tasksFlow: Flow<List<Task>> = combine(
        hideCompleted,
        todayTasksFlow,
        categoriesFlow,
        ::mergeTodayFlows
    )

    val todayTasks = tasksFlow.asLiveData()

    private fun mergeTodayFlows(
        hideCompleted: Boolean,
        tasks: List<Task>,
        categories: List<Category>
    ): List<Task> {
        val tasksWithCategoryData: MutableList<Task> = mutableListOf()

        tasks
            .filter { task ->
                task.completed == hideCompleted
            }
            .forEach { task ->
                categories.forEach { category ->
                    if (category.categoryId == task.categoryId) {
                        tasksWithCategoryData.add(
                            task.copy(categoryName = category.categoryName, color = category.color)
                        )
                    }
                }
            }

        return tasksWithCategoryData
    }
}
