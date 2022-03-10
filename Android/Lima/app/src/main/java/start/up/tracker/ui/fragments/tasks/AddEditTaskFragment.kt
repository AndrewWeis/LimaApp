package start.up.tracker.ui.fragments.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.EditTaskFragmentBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.AddEditTaskViewModel
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.edit_task.EditTaskAdapter
import start.up.tracker.ui.list.generators.tasks.EditTaskInfoGenerator

@AndroidEntryPoint
class AddEditTaskFragment :
    Fragment(R.layout.edit_task_fragment) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    private var listExtension: ListExtension? = null
    private var binding: EditTaskFragmentBinding? = null
    private lateinit var adapter: EditTaskAdapter
    private val generator: EditTaskInfoGenerator = EditTaskInfoGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EditTaskFragmentBinding.bind(view)

        initAdapter()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    private fun initAdapter() {
        adapter = EditTaskAdapter(
            layoutInflater = layoutInflater
        )

        listExtension = ListExtension(binding?.editTasksList)
        listExtension?.setLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun initObservers() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) { task ->
            showFields(task)
        }
    }

    private fun showFields(task: Task?) {
        adapter.addListItems(generator.createListItems(task))
    }
}
