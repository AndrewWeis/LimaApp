package start.up.tracker.ui.list.view_holders.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.TasksItemBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.base.BaseTasksOperationsViewModel
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.data.entities.tasks.TasksData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.tasks.TaskAdapter
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class TasksViewHolder(
    private val layoutInflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder(layoutInflater, parent, R.layout.tasks_item),
    OnTaskClickListener {

    private var binding: TasksItemBinding = TasksItemBinding.bind(itemView)

    private lateinit var listItem: ListItem
    private lateinit var data: TasksData

    private lateinit var listener: OnTaskClickListener
    private lateinit var adapter: TaskAdapter
    private var listExtension: ListExtension? = null

    init {
        initAdapter()
    }

    override fun onTaskClick(task: Task) {
        listener.onTaskClick(task)
    }

    override fun onCheckBoxClick(task: Task) {
        listener.onCheckBoxClick(task)
    }

    fun bind(listItem: ListItem, viewModel: BaseTasksOperationsViewModel, listener: OnTaskClickListener) {
        this.listItem = listItem
        this.data = listItem.data as TasksData
        this.listener = listener

        updateItems()
        attachSwipeToDeleteTask(viewModel)
    }

    private fun attachSwipeToDeleteTask(viewModel: BaseTasksOperationsViewModel) {
        listExtension?.attachSwipeToAdapter(adapter, viewModel)
    }

    private fun updateItems() {
        val tasksListItems = data.tasks.map {
            val item = ListItem(
                id = ListItemIds.SUBTASK,
                type = ListItemTypes.TASK,
                data = it
            )
            item
        }

        adapter.updateItems(tasksListItems)
    }

    private fun initAdapter() {
        adapter = TaskAdapter(
            layoutInflater = layoutInflater,
            listener = this
        )

        listExtension = ListExtension(binding.list)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }
}
