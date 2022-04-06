package start.up.tracker.mvvm.view_models.edit_task.dialogs

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.entities.Project
import start.up.tracker.ui.data.entities.edit_task.ProjectData
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class ProjectsDialogViewModel @Inject constructor(
    projectsDao: ProjectsDao,
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    val projectId = state.getLiveData<Int>(StateHandleKeys.SELECTED_PROJECT_ID)

    private val projectsFlow = projectsDao.getProjects()
    private val projectsMergedFlow: Flow<List<ProjectData>> = combine(
        projectId.asFlow(),
        projectsFlow,
        ::mergeProjectsFlows
    )
    private var _projects: LiveData<List<ProjectData>> = MutableLiveData()
    val projects: LiveData<List<ProjectData>> get() = _projects

    init {
        showProjectData()
    }

    fun onProjectClick(selectedProjectId: Int) {
        projectId.postValue(selectedProjectId)
    }

    /**
     * Соединяет flow проектов, полученних их базы данных и flow идентификатора выбранного проекта
     *
     * @param selectedProjectId идентификатор выбранного проекта
     * @param projects список проектов
     * @return projectData с информацией о выбранном проекте
     */
    private fun mergeProjectsFlows(
        selectedProjectId: Int,
        projects: List<Project>
    ): List<ProjectData> {

        return projects.map { project ->
            var isSelected = false
            if (project.projectId == selectedProjectId) {
                isSelected = true
            }

            val projectData = ProjectData(
                id = project.projectId,
                title = project.projectTitle,
                color = project.color,
                isSelected = isSelected
            )

            projectData
        }
    }

    private fun showProjectData() {
        _projects = projectsMergedFlow.asLiveData()
    }
}
