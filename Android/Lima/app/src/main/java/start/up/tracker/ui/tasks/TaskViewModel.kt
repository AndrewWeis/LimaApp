package start.up.tracker.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.data.db.Task
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.repository.TaskRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
    ) : ViewModel() {

}