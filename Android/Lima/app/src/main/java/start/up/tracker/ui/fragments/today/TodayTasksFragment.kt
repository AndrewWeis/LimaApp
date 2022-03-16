package start.up.tracker.ui.fragments.today

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.databinding.FragmentTodayTasksBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.today.TodayTasksViewModel
import start.up.tracker.ui.data.entities.TasksEvent
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.tasks.ProjectTasksFragmentDirections
import start.up.tracker.ui.fragments.tasks.base.BaseTasksFragment
import start.up.tracker.ui.list.adapters.tasks.TaskAdapter
import start.up.tracker.ui.list.generators.tasks.TasksGenerator
import start.up.tracker.ui.list.view_holders.tasks.OnTaskClickListener
import start.up.tracker.utils.resources.ResourcesUtils

@AndroidEntryPoint
class TodayTasksFragment :
    BaseTasksFragment(R.layout.fragment_today_tasks),
    OnTaskClickListener {

    private val viewModel: TodayTasksViewModel by viewModels()

    private var binding: FragmentTodayTasksBinding? = null

    private lateinit var adapter: TaskAdapter
    private var listExtension: ListExtension? = null
    private val generator: TasksGenerator = TasksGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTodayTasksBinding.bind(view)

        initAdapter()
        initListeners()
        initObservers()
        initTaskEventListener()

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onTaskClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task) {
        viewModel.onTaskCheckedChanged(task)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_today_tasks, menu)

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.hideCompleted.first()
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

    private fun showTasks(tasks: List<Task>) {
        adapter.addListItems(generator.createListItems(tasks))
    }

    private fun initTaskEventListener() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.tasksEvent.collect { event ->
            when (event) {
                is TasksEvent.ShowUndoDeleteTaskMessage -> {
                    showUndoDeleteSnackbar { viewModel.onUndoDeleteTaskClick(event.task) }
                }
                is TasksEvent.NavigateToAddTaskScreen -> {
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = ResourcesUtils.getString(R.string.title_add_task),
                        categoryId = 1
                    )
                    navigateTo(action)
                }
                is TasksEvent.NavigateToEditTaskScreen -> {
                    val action = TodayFragmentDirections.actionTodayToAddEditTask(
                        title = ResourcesUtils.getString(R.string.title_edit_task),
                        categoryId = event.task.categoryId,
                        task = event.task
                    )
                    navigateTo(action)
                }
                is TasksEvent.ShowTaskSavedConfirmationMessage -> {
                    showTaskSavedMessage(event.msg)
                }
                is TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                    val action =
                        ProjectTasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
                    navigateTo(action)
                }
            }
        }
    }

    private fun initListeners() {
        binding?.addTaskFab?.setOnClickListener {
            viewModel.onAddNewTaskClick()
        }
    }

    private fun initObservers() {
        viewModel.todayTasks.observe(viewLifecycleOwner) {
            showTasks(it)
        }
    }

    private fun initAdapter() {
        adapter = TaskAdapter(
            layoutInflater = layoutInflater,
            listener = this
        )

        listExtension = ListExtension(binding?.todayTasksList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)

        listExtension?.attachSwipeToAdapter(adapter, viewModel)
    }
}
