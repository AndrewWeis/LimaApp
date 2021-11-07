package start.up.tracker.ui.categoryInside

import android.util.Log
import android.view.animation.Transformation
import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.data.db.Task
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.db.models.Category
import javax.inject.Inject

@HiltViewModel
class CategoryInsideViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val category = state.get<Category>("category")

    var categoryName = state.get<String>("categoryName") ?: category?.categoryName ?: ""
    set(value) {
        field = value
        state.set("categoryName", value)
    }

    private val tasksOfCategory = taskDao.getTasksOfCategory(categoryName)

    /**
     * Transforms LiveData<List<CategoryWithTask>> to LiveData<List<Task>>
     * We need this transformation for passing all tasks of specific category to [TaskAdapter]
     */
    val tasks: LiveData<List<Task>> =
        Transformations.map(tasksOfCategory) { taskList ->
            var newData: List<Task> = listOf()
            taskList?.forEach {
                newData = it.tasks
            }
            return@map newData
        }
}