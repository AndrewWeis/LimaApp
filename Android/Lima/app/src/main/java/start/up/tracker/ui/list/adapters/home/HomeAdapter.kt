package start.up.tracker.ui.list.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.mvvm.view_models.home.HomeViewModel
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.adapters.base.BaseSequenceAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.headers.HeaderViewHolder
import start.up.tracker.ui.list.view_holders.home.HomeSectionViewHolder
import start.up.tracker.ui.list.view_holders.home.ProjectViewHolder
import start.up.tracker.ui.list.view_holders.home.ProjectsViewHolder

class HomeAdapter(
    layoutInflater: LayoutInflater,
    private val viewModel: HomeViewModel,
    private val onHomeSectionClickListener: HomeSectionViewHolder.OnHomeSectionClickListener,
    private val onProjectClickListener: ProjectViewHolder.OnProjectClickListener
) : BaseSequenceAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ITEM_INBOX, ITEM_TODAY, ITEM_UPCOMING, ITEM_POMODORO ->
                return HomeSectionViewHolder(layoutInflater, parent)
            ITEM_HEADER_PROJECTS ->
                return HeaderViewHolder(layoutInflater, parent)
            ITEM_PROJECTS_LIST ->
                return ProjectsViewHolder(layoutInflater, parent)
            else -> throwUnknownViewHolderTypeException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        when (holder.itemViewType) {
            ITEM_INBOX, ITEM_TODAY, ITEM_UPCOMING, ITEM_POMODORO ->
                (holder as HomeSectionViewHolder).bind(item, onHomeSectionClickListener)
            ITEM_HEADER_PROJECTS ->
                (holder as HeaderViewHolder).bind(item)
            ITEM_PROJECTS_LIST ->
                (holder as ProjectsViewHolder).bind(item, viewModel, onProjectClickListener)
        }
    }

    override fun getItemViewType(item: ListItem): Int {
        return when (item.type) {
            ListItemTypes.HOME_BLOCK -> getProjectItemViewType(item)
            ListItemTypes.HEADER -> ITEM_HEADER_PROJECTS
            ListItemTypes.LIST -> ITEM_PROJECTS_LIST
            else -> NOT_FOUND
        }
    }

    override fun getTypeSequence() = intArrayOf(
        ITEM_INBOX,
        ITEM_TODAY,
        ITEM_UPCOMING,
        ITEM_POMODORO,
        ITEM_HEADER_PROJECTS,
        ITEM_PROJECTS_LIST,
    )

    fun setUpcomingListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_UPCOMING)
    }

    fun setTodayListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_TODAY)
    }

    fun setInboxListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_INBOX)
    }

    fun setProjectsListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_PROJECTS_LIST)
    }

    fun setProjectsHeaderListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_HEADER_PROJECTS)
    }

    fun setPomodoroListItem(listItem: ListItem) {
        updateItem(listItem, ITEM_POMODORO)
    }

    private fun getProjectItemViewType(item: ListItem): Int {
        return when (item.id) {
            ListItemIds.TODAY -> ITEM_TODAY
            ListItemIds.UPCOMING -> ITEM_UPCOMING
            ListItemIds.INBOX -> ITEM_INBOX
            ListItemIds.POMODORO -> ITEM_POMODORO
            else -> NOT_FOUND
        }
    }

    private companion object {
        const val ITEM_INBOX = 0
        const val ITEM_TODAY = 1
        const val ITEM_UPCOMING = 2
        const val ITEM_POMODORO = 3
        const val ITEM_HEADER_PROJECTS = 4
        const val ITEM_PROJECTS_LIST = 5
    }
}
