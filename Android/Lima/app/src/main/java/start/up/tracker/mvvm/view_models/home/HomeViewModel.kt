package start.up.tracker.mvvm.view_models.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.database.dao.ProjectsDao
import start.up.tracker.database.dao.TaskDao
import start.up.tracker.database.dao.TodayTasksDao
import start.up.tracker.database.dao.UpcomingTasksDao
import start.up.tracker.entities.Project
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.home.HomeSection
import start.up.tracker.ui.data.entities.home.ProjectsData
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.resources.ResourcesUtils
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val projectsDao: ProjectsDao,
    todayTasksDao: TodayTasksDao,
    upcomingTasksDao: UpcomingTasksDao,
) : ViewModel() {

    private val projectsFlow: Flow<ProjectsData> = projectsDao.getProjects()
        .transform { projects ->
            val newProjects = projects.filter { projectId ->
                projectId.projectId != INBOX_ID
            }
            val projectsData = ProjectsData(projects = newProjects)
            emit(projectsData)
        }
    val projects = projectsFlow.asLiveData()

    private val projectEventsChannel = Channel<HomeEvents>()
    val projectEvents = projectEventsChannel.receiveAsFlow()

    private val inboxSectionFlow: Flow<HomeSection> = taskDao.countTasksOfInbox()
        .transform { tasksInside ->
            val inboxSection = createHomeSection(R.drawable.ic_inbox, R.string.inbox, tasksInside)
            emit(inboxSection)
        }
    val inboxSection = inboxSectionFlow.asLiveData()

    private val todaySectionFlow: Flow<HomeSection> = todayTasksDao.countTodayTasks(
        today = TimeHelper.getCurrentDayInMilliseconds()
    ).transform { tasksInside ->
        val inboxSection = createHomeSection(R.drawable.ic_today, R.string.today, tasksInside)
        emit(inboxSection)
    }
    val todaySection = todaySectionFlow.asLiveData()

    private val upcomingSectionFlow: Flow<HomeSection> = upcomingTasksDao.countUpcomingTasks(
        today = TimeHelper.getCurrentDayInMilliseconds(),
    ).transform { tasksInside ->
        val inboxSection = createHomeSection(R.drawable.ic_upcoming, R.string.upcoming, tasksInside)
        emit(inboxSection)
    }
    val upcomingSection = upcomingSectionFlow.asLiveData()

    fun updateNumberOfTasks() = viewModelScope.launch {
        projects.value?.projects?.forEach { project ->
            val number = taskDao.countTasksOfProject(project.projectId)
            projectsDao.updateProject(project.copy(tasksInside = number))
        }
    }

    fun onAddProjectClick() = viewModelScope.launch {
        projectEventsChannel.send(HomeEvents.NavigateToAddProject)
    }

    fun onProjectClicked(project: Project) = viewModelScope.launch {
        projectEventsChannel.send(HomeEvents.NavigateToProject(project))
    }

    fun onHomeSectionClick(listItem: ListItem) = viewModelScope.launch {
        when (listItem.id) {
            ListItemIds.INBOX -> {
                val inbox = Project(projectId = 1, projectTitle = ResourcesUtils.getString(R.string.inbox))
                projectEventsChannel.send(HomeEvents.NavigateToProject(inbox))
            }
            ListItemIds.TODAY ->
                projectEventsChannel.send(HomeEvents.NavigateToToday)
            ListItemIds.UPCOMING ->
                projectEventsChannel.send(HomeEvents.NavigateToUpcoming)
        }
    }

    private fun createHomeSection(iconRes: Int, stringRes: Int, tasksInside: Int): HomeSection {
        return HomeSection(
            iconRes = iconRes,
            title = ResourcesUtils.getString(stringRes),
            numberOfTasksInside = tasksInside
        )
    }

    sealed class HomeEvents {
        data class NavigateToProject(val project: Project) : HomeEvents()
        object NavigateToAddProject : HomeEvents()
        object NavigateToToday : HomeEvents()
        object NavigateToUpcoming : HomeEvents()
    }

    private companion object {
        const val INBOX_ID = 1
    }
}
