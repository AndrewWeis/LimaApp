package start.up.tracker.ui.categories

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.data.db.TaskDao
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val taskDao: TaskDao,
) : ViewModel() {

    val categories = taskDao.getCategories().asLiveData()
}