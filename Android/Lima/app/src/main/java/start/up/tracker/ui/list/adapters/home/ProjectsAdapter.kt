package start.up.tracker.ui.list.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.adapters.base.BaseAdapter
import start.up.tracker.ui.list.diff_utils.projects.ProjectDiffUtils
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.home.ProjectViewHolder

class ProjectsAdapter(
    layoutInflater: LayoutInflater,
    private val onProjectClickListener: ProjectViewHolder.OnProjectClickListener,
) : BaseAdapter<ListItem, BaseViewHolder>(layoutInflater) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder =
        ProjectViewHolder(layoutInflater, parent)

    override fun onBindViewHolder(holder: BaseViewHolder, item: ListItem) {
        (holder as ProjectViewHolder).bind(item, onProjectClickListener)
    }

    override fun updateItems(items: List<ListItem>) {
        val diffUtils = ProjectDiffUtils(getItems(), items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        getItems().clear()
        getItems().addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}
