package start.up.tracker.ui.today

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
import start.up.tracker.data.db.SortOrder
import start.up.tracker.data.models.Task
import start.up.tracker.data.models.TodayTask
import start.up.tracker.databinding.FragmentTodayTasksBinding
import start.up.tracker.ui.projectstasks.ProjectsTasksFragmentDirections
import start.up.tracker.ui.projectstasks.ProjectsTasksViewModel
import start.up.tracker.utils.exhaustive
import start.up.tracker.utils.onQueryTextChanged
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TodayTasksFragment : Fragment(R.layout.fragment_today_tasks), TodayTasksAdapter.OnItemClickListener {

    private val viewModel: TodayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTodayTasksBinding.bind(view)

        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val currentDate: String = formatter.format(Date())

        binding.txTaskToday.text = currentDate

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

        viewModel.todayTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is TodayViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.todayTask)
                            }.show()
                    }
                    is TodayViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                      //  val action = ProjectsTasksFragmentDirections.actionCategoryInsideFragmentToAddEditTaskFragment(title = "Add new task", categoryName = viewModel.categoryName)
                       // findNavController().navigate(action)
                    }
                    is TodayViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        //val action = ProjectsTasksFragmentDirections.actionCategoryInsideFragmentToAddEditTaskFragment(event.todayTask, "Edit task", viewModel.categoryName)
                        //findNavController().navigate(action)
                    }
                    is TodayViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is TodayViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action = ProjectsTasksFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
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

    override fun onItemClick(todayTask: TodayTask) {
           viewModel.onTaskSelected(todayTask)
    }

    override fun onCheckBoxClick(todayTask: TodayTask, isChecked: Boolean) {
         viewModel.onTaskCheckedChanged(todayTask, isChecked)
    }
}