package start.up.tracker.ui.list.view_holders.home

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.ProjectsItemBinding
import start.up.tracker.entities.Project
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.home.ProjectsData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.home.ProjectsAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder
import start.up.tracker.ui.list.view_holders.home.ProjectViewHolder.OnProjectClickListener

class ProjectsViewHolder(
    private val layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.projects_item),
    OnProjectClickListener {

    private var binding = ProjectsItemBinding.bind(itemView)

    private lateinit var listItem: ListItem
    private lateinit var data: ProjectsData

    private lateinit var listener: OnProjectClickListener
    private lateinit var adapter: ProjectsAdapter
    private var listExtension: ListExtension? = null

    init {
        initAdapter()
    }

    override fun onProjectClick(project: Project) {
        listener.onProjectClick(project)
    }

    fun bind(listItem: ListItem, listener: OnProjectClickListener) {
        this.listItem = listItem
        this.data = listItem.data as ProjectsData
        this.listener = listener

        updateItems()
    }

    private fun updateItems() {
        val projectsListItems = data.projects.map {
            val item = ListItem(
                id = listItem.id,
                type = ListItemTypes.PROJECT,
                data = it
            )
            item
        }

        adapter.updateItems(projectsListItems)
    }

    private fun initAdapter() {
        adapter = ProjectsAdapter(
            layoutInflater = layoutInflater,
            onProjectClickListener = this
        )

        listExtension = ListExtension(binding.projectsList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }
}
