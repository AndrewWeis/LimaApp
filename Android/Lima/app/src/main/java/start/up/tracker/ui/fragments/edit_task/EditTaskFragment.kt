package start.up.tracker.ui.fragments.edit_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.data.fields.Field
import start.up.tracker.databinding.EditTaskFragmentBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.edit_task.EditTaskViewModel
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.chips.ChipData
import start.up.tracker.ui.data.entities.chips.ChipsData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseFragment
import start.up.tracker.ui.list.adapters.edit_task.EditTaskAdapter
import start.up.tracker.ui.list.generators.edit_task.EditTaskInfoGenerator
import start.up.tracker.ui.list.view_holders.edit_task.ChipsViewHolder
import start.up.tracker.ui.list.view_holders.forms.SelectInputViewHolder
import start.up.tracker.ui.views.forms.base.BaseInputView
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.screens.RequestCodes
import start.up.tracker.utils.screens.ResultCodes
import java.util.*

@AndroidEntryPoint
class EditTaskFragment :
    BaseFragment(R.layout.edit_task_fragment),
    BaseInputView.TextInputListener,
    SelectInputViewHolder.TextInputSelectionListener,
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener,
    ChipsViewHolder.CategoriesViewHolderListener {

    private val viewModel: EditTaskViewModel by viewModels()

    private var binding: EditTaskFragmentBinding? = null

    private lateinit var adapter: EditTaskAdapter
    private var listExtension: ListExtension? = null
    private val generator: EditTaskInfoGenerator = EditTaskInfoGenerator()

    // todo(костыльное решение, подумать как можно сделать по-другому)
    private var isTaskTimeTypeStart = true

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
            ListItemIds.TASK_TITLE -> viewModel.onTaskTitleChanged(value)
            ListItemIds.TASK_DESCRIPTION -> viewModel.onTaskDescriptionChanged(value)
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
        when (listItem.id) {
            ListItemIds.TASK_TIME_START -> {
                isTaskTimeTypeStart = true
                openTimePicker()
            }
            ListItemIds.TASK_TIME_END -> {
                isTaskTimeTypeStart = false
                openTimePicker()
            }
            ListItemIds.TASK_DATE ->
                openDatePicker()
        }
    }

    override fun onTimeSet(timePicker: TimePicker, hours: Int, minutes: Int) {
        if (isTaskTimeTypeStart) {
            viewModel.onTaskStartTimeChanged(hours * 60 + minutes)
        } else {
            viewModel.onTaskEndTimeChanged(hours * 60 + minutes)
        }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val milliseconds = TimeHelper.getDateInMilliseconds(year, month, dayOfMonth)
        viewModel.onTaskDateChanged(milliseconds)
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DatePicker, this,
            calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )

        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis

        datePickerDialog.show()
    }

    private fun openTimePicker() {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            requireContext(), R.style.DatePicker, this,
            calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], TimeHelper.isSystem24Hour
        )

        timePickerDialog.show()
    }

    private fun showCategories(chips: ChipsData) {
        val listItem: ListItem = generator.createCategoriesChipsListItems(chips)

        if (binding?.editTasksList?.isComputingLayout == false) {
            adapter.setCategoryChipListItem(listItem)
            return
        }

        binding?.editTasksList?.post {
            adapter.setCategoryChipListItem(listItem)
        }
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
        adapter.updateItems(generator.createEditableTaskInfoListItems(task))
    }

    private fun initAdapter() {
        adapter = EditTaskAdapter(
            layoutInflater = layoutInflater,
            textInputListener = this,
            textInputSelectionListener = this,
            categoriesViewHolderListener = this
        )

        listExtension = ListExtension(binding?.editTasksList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun initObservers() {
        viewModel.taskInfoLiveData.observe(viewLifecycleOwner) { task ->
            showEditableTaskInfo(task)
        }

        viewModel.titleField.observe(viewLifecycleOwner) { field ->
            showTitleField(field)
        }

        viewModel.categoriesChips.observe(viewLifecycleOwner) { chips ->
            showCategories(chips)
        }
    }

    private fun initEventsListener() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.editTaskEvent.collect { event ->
            when (event) {
                is EditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                    setFragmentResult(
                        RequestCodes.EDIT_TASK,
                        bundleOf(ResultCodes.EDIT_TASK to event.result)
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

    override fun onChipClick(chipData: ChipData) {
        TODO("Not yet implemented")
    }
}
