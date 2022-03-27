package start.up.tracker.ui.list.view_holders.home

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.ProjectItemBinding
import start.up.tracker.entities.Project
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class ProjectViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.project_item) {

    private val binding = ProjectItemBinding.bind(itemView)

    private lateinit var project: Project
    private lateinit var listener: OnProjectClickListener

    fun bind(listItem: ListItem, listener: OnProjectClickListener) {
        this.project = listItem.data as Project
        this.listener = listener

        setupData()
        setupListeners()
    }

    private fun setupData() {
        binding.projectTitleText.text = project.projectTitle
        binding.tasksNumberText.text = project.tasksInside.toString()
        binding.projectImage.background.setTint(project.color)
    }

    private fun setupListeners() {
        binding.root.setOnClickListener {
            listener.onProjectClick(project)
        }
    }

    interface OnProjectClickListener {
        fun onProjectClick(project: Project)
    }
}
