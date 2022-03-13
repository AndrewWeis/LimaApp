package start.up.tracker.ui.fragments.tasks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.data.fields.Field
import start.up.tracker.databinding.EditTaskFragmentBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.tasks.AddEditTaskViewModel
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseFragment
import start.up.tracker.ui.list.adapters.edit_task.EditTaskAdapter
import start.up.tracker.ui.list.generators.tasks.EditTaskInfoGenerator
import start.up.tracker.ui.list.view_holders.forms.SelectInputViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView

@AndroidEntryPoint
class AddEditTaskFragment :
    BaseFragment(R.layout.edit_task_fragment),
    BaseInputView.TextInputListener,
    SelectInputViewHolder.TextInputSelectionListener {

    private val viewModel: AddEditTaskViewModel by viewModels()

    private var binding: EditTaskFragmentBinding? = null

    private lateinit var adapter: EditTaskAdapter
    private var listExtension: ListExtension? = null
    private val generator: EditTaskInfoGenerator = EditTaskInfoGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EditTaskFragmentBinding.bind(view)

        initAdapter()
        initListeners()
        initObservers()
        initEventsListener()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onFinishedEditingTask()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onTextInputDataChange(listItem: ListItem) {
        val value = listItem.data as String
        when (listItem.id) {
            ListItemIds.TASK_TITLE -> viewModel.onTaskTitleHasBeenChanged(value)
            ListItemIds.TASK_DESCRIPTION -> viewModel.onTaskDescriptionHasBeenChanged(value)
        }
    }

    override fun onFocusLose(listItem: ListItem) {
        hideKeyboard()
        binding?.content?.requestFocus()
    }

    override fun onFocusGain(listItem: ListItem) {
        /*
         * Код добавлен, так как не всегда при захвате фокуса показывается клавиатура.
         * Например, когда используется функция copy-paste
         */
        showKeyboard()
    }

    override fun onClearClick(listItem: ListItem) {
        when (listItem.id) {
            ListItemIds.TASK_TITLE -> viewModel.onTaskTitleClearClick()
            ListItemIds.TASK_DESCRIPTION -> viewModel.onTaskDescriptionClearClick()
        }
    }

    override fun onDoneClick(item: ListItem) {
        hideKeyboard()
    }

    override fun onTextInputSelectionClick(listItem: ListItem) {
        TODO("Not yet implemented")
    }

    private fun showTitleField(field: Field<String>) {
        val listItem: ListItem = generator.createTitleListItem(field)

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setTitleItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setTitleItem(listItem)
        }
    }

    private fun showEditableTaskInfo(task: Task?) {
        adapter.addListItems(generator.createListItems(task))
    }

    private fun initAdapter() {
        adapter = EditTaskAdapter(
            layoutInflater = layoutInflater,
            textInputListener = this,
            textInputSelectionListener = this
        )

        listExtension = ListExtension(binding?.editTasksList)
        listExtension?.setLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun initObservers() {
        viewModel.taskInfoLiveData.observe(viewLifecycleOwner) { task ->
            showEditableTaskInfo(task)
        }

        viewModel.titleField.observe(viewLifecycleOwner) { field ->
            showTitleField(field)
        }
    }

    private fun initEventsListener() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.addEditTaskEvent.collect { event ->
            when (event) {
                is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                    setFragmentResult(
                        "add_edit_request",
                        bundleOf("add_edit_result" to event.result)
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun initListeners() {
        binding?.doneButton?.setOnClickListener {
            viewModel.onSaveClick()
        }
    }
}
