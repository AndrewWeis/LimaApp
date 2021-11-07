package start.up.tracker.ui.categoryInside

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.data.db.Task
import start.up.tracker.databinding.FragmentCategoryInsideBinding
import start.up.tracker.ui.tasks.TasksAdapter
import start.up.tracker.ui.tasks.TasksViewModel

/**
 * This class uses [TasksViewModel] because it have similiar functionality to [TasksFragment]
 */
@AndroidEntryPoint
class CategoryInsideFragment : Fragment(R.layout.fragment_category_inside), TasksAdapter.OnItemClickListener{

    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCategoryInsideBinding.bind(view)

        val taskAdapter = TasksAdapter(this)

        binding.apply {
            categoryInsideRv.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.tasksOfCategory.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

    }

    override fun onItemClick(task: Task) {
        TODO("Not yet implemented")
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        TODO("Not yet implemented")
    }

}