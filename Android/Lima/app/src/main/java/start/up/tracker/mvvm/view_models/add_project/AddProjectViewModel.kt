package start.up.tracker.mvvm.view_models.add_project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.entities.Project
import start.up.tracker.ui.data.entities.add_project.ColorData
import start.up.tracker.ui.data.entities.add_project.ColorsData
import start.up.tracker.utils.resources.ResourcesUtils
import javax.inject.Inject

@HiltViewModel
class AddProjectViewModel @Inject constructor(
    private val projectsDao: ProjectsDao,
) : ViewModel() {

    private val addCategoryEventChannel = Channel<AddProjectEvent>()
    val addCategoryEvent = addCategoryEventChannel.receiveAsFlow()

    private var project = Project()

    private val _projectTitle: MutableLiveData<Project> = MutableLiveData()
    val projectTitle: LiveData<Project> get() = _projectTitle

    private val _projectActions: MutableLiveData<Boolean> = MutableLiveData(false)
    val projectActions: LiveData<Boolean> get() = _projectActions

    private val _colorsCircles: MutableLiveData<ColorsData> = MutableLiveData()
    val colorsCircles: LiveData<ColorsData> get() = _colorsCircles

    init {
        showFields()
    }

    private fun showFields() {
        showProjectTitle()
        showColors()
    }

    private fun showColors() {
        val colors: MutableList<ColorData> = mutableListOf()

        val list = ResourcesUtils.getValuesFromResArray(R.array.default_colors)
        for (color in list) {
            val colorData = getColorData(color)
            colors.add(colorData)
        }

        _colorsCircles.postValue(ColorsData(values = colors))
    }

    private fun getColorData(colorRes: Int) = ColorData(
        colorRes = colorRes,
        isSelected = project.color == colorRes
    )

    private fun showProjectTitle() {
        _projectTitle.postValue(project)
    }

    fun onDoneButtonClicked() = viewModelScope.launch {
        projectsDao.insertProject(project)
        addCategoryEventChannel.send(AddProjectEvent.NavigateBack)
    }

    fun onProjectTitleChanged(title: String) {
        project = project.copy(projectTitle = title)

        if (project.projectTitle.isEmpty()) {
            _projectActions.postValue(true)
        }

        if (project.projectTitle.length == 1) {
            _projectActions.postValue(false)
        }
    }

    fun onBackButtonClick() = viewModelScope.launch {
        addCategoryEventChannel.send(AddProjectEvent.NavigateBack)
    }

    fun onColorClick(colorData: ColorData) {
        project = project.copy(color = colorData.colorRes)
        showColors()
    }

    fun onTitleClearClick() {
        project = project.copy(projectTitle = "")
    }

    sealed class AddProjectEvent {
        object NavigateBack : AddProjectEvent()
    }
}
