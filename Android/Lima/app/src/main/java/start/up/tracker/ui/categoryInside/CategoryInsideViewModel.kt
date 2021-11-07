package start.up.tracker.ui.categoryInside

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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


}