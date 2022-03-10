package start.up.tracker.ui.fragments.today

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.entities.Task
import start.up.tracker.databinding.FragmentTodayTasksBinding
import start.up.tracker.mvvm.view_models.today.TodayTasksViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.fragments.BaseTasksFragment
import start.up.tracker.ui.fragments.tasks.ProjectsTasksFragmentDirections
import start.up.tracker.ui.list.adapters.TodayTasksAdapter

@AndroidEntryPoint
class TodayTasksFragment :
    BaseTasksFragment(R.layout.fragment_today_tasks),
    TodayTasksAdapter.OnItemClickListener {

    private val viewModel: TodayTasksViewModel by viewModels()

    private var binding: FragmentTodayTasksBinding? = null
    private lateinit var taskAdapter: TodayTasksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTodayTasksBinding.bind(view)

        initAdapter()
        initListeners()
        initTaskEventListener()

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_today_tasks, menu)

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.hideCompleted.first() ?: false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                viewModel.onDeleteAllCompletedClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initTaskEventListener() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.tasksEvent.collect { event ->
            when (event) {
                is TasksEvent.ShowUndoDeleteTaskMessage -> {
                    showUndoDeleteSnackbar { viewModel.onUndoDeleteTaskClick(event.task) }
                }
                is TasksEvent.NavigateToAddTaskScreen -> {
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = "Add new task",
                        categoryId = 1
                    )
                    navigateTo(action)
                }
                is TasksEvent.NavigateToEditTaskScreen -> {
                    val task = event.task
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = "Edit task",
                        categoryId = event.task.categoryId,
                        task = task
                    )
                    navigateTo(action)
                }
                is TasksEvent.ShowTaskSavedConfirmationMessage -> {
                    showTaskSavedMessage(event.msg)
                }
                is TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                    val action =
                        ProjectsTasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
                    navigateTo(action)
                }
            }
        }
    }

    private fun initListeners() {
        binding?.addTaskOfToday?.setOnClickListener {
            viewModel.onAddNewTaskClick()
        }

        viewModel.todayTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }

    private fun initAdapter() {
        taskAdapter = TodayTasksAdapter(this)

        binding?.todayTaskRV?.apply {
            itemAnimator = null
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        attachSwipeToAdapter()
    }

    private fun attachSwipeToAdapter() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val todayTask = taskAdapter.currentList[viewHolder.adapterPosition]
                viewModel.onTaskSwiped(todayTask)
            }
        }).attachToRecyclerView(binding?.todayTaskRV)
    }
}
