package start.up.tracker.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_today_tasks.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.data.entities.ExtendedTask
import start.up.tracker.databinding.FragmentTodayTasksBinding
import start.up.tracker.mvvm.view_models.BaseViewModel
import start.up.tracker.ui.list.adapters.TodayTasksAdapter
import start.up.tracker.mvvm.view_models.TodayViewModel
import start.up.tracker.utils.exhaustive
import start.up.tracker.utils.toTask

@AndroidEntryPoint
class TodayTasksFragment : Fragment(R.layout.fragment_today_tasks),
    TodayTasksAdapter.OnItemClickListener {

    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTodayTasksBinding.bind(view)

        val taskAdapter = TodayTasksAdapter(this)
        binding.todayTaskRV.apply {
            itemAnimator = null
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
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
            }).attachToRecyclerView(todayTaskRV)
        }

        binding.addTaskOfToday.setOnClickListener {
            viewModel.onAddNewTaskClick()
        }

        viewModel.todayTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is BaseViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.extendedTask)
                            }.show()
                    }
                    is BaseViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action = TodayFragmentDirections.actionTodayToAddEditTask(title = "Add new task", categoryId = 1)
                        findNavController().navigate(action)
                    }
                    is BaseViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val task = event.extendedTask.toTask()
                        val action = TodayFragmentDirections.actionTodayToAddEditTask(title = "Edit task", categoryId = event.extendedTask.categoryId, task = task)
                        findNavController().navigate(action)
                    }
                    is BaseViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is BaseViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action = ProjectsTasksFragmentDirections.actionGlobalDeleteAllCompletedDialog()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_today_tasks, menu)

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.hideCompleted.first() ?: false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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

    override fun onItemClick(extendedTask: ExtendedTask) {
           viewModel.onTaskSelected(extendedTask)
    }

    override fun onCheckBoxClick(extendedTask: ExtendedTask, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(extendedTask, isChecked)
    }
}