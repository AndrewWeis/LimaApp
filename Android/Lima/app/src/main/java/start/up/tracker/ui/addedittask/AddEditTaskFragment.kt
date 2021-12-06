package start.up.tracker.ui.addedittask

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.databinding.FragmentAddEditTaskBinding
import start.up.tracker.utils.exhaustive
import java.text.SimpleDateFormat
import kotlin.properties.Delegates.notNull

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    //TODO(BUG when tasks have the same name)
    private val viewModel: AddEditTaskViewModel by viewModels()
    private var date by notNull<String>()
    private var timeStart by notNull<String>()
    private var timeEnd by notNull<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        date = viewModel.taskDate
        timeStart = viewModel.taskTimeStart
        timeEnd = viewModel.taskTimeEnd

        binding.apply {
            editTextTaskLabel.setText(viewModel.taskName)

            editTextTaskLabel.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            btnDatePicker.text = viewModel.taskDate
            btnTimeStart.text = viewModel.taskTimeStart
            btnTimeEnd.text = viewModel.taskTimeEnd


            fabSaveTask.setOnClickListener {
                val checkedChip = binding.chipCategoriesGroup.children
                    .toList()
                    .filter { (it as Chip).isChecked }
                    .joinToString { (it as Chip).text }

                val checkedPriority = binding.chipPriorityGroup.children
                    .toList()
                    .filter { (it as Chip).isChecked }
                    .joinToString { (it as Chip).text }

                val priority = viewModel.priorityToInt(checkedPriority)

                viewModel.onSaveClick(checkedChip, date, timeStart, timeEnd, priority)
            }


            chipPriorityGroup.isSingleSelection = true
            chipPriorityGroup.isSelectionRequired = true
            chipPriorityGroup.forEach {
                val textPriority = (it as Chip).text.toString()
                val priority = viewModel.priorityToInt(textPriority)
                if (viewModel.taskPriority == priority) {
                    it.isChecked = true
                }
            }



            btnDatePicker.setOnClickListener {
                openDatePicker(binding)
            }

            btnTimeStart.setOnClickListener {
                openTimePicker(binding, "Time Start")
            }

            btnTimeEnd.setOnClickListener {
                openTimePicker(binding, "Time End")
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.editTextTaskLabel.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }


        /**
         * Dynamically add chips into [ChipGroup]. It checks if the name of category from all categories is in
         * checked. If it is, it means that current task is associated with that category, so
         * we need to isChipChecked = true
         */
        viewModel.combinedCategories.observe(viewLifecycleOwner) {
            val set = it.first
            val subset = it.second

            set.forEach { category ->
                var isChipChecked = false
                if (subset.categoryName == category.categoryName) {
                    isChipChecked = true
                }
                addCategoryChip(category.categoryName, binding.chipCategoriesGroup, isChipChecked)
            }
        }
    }

    private fun openTimePicker(binding: FragmentAddEditTaskBinding, title: String) {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText(title)
            .build()
        picker.show(childFragmentManager, "TIME_PICKER")

        picker.addOnPositiveButtonClickListener {
            var hour = picker.hour.toString()
            var minute = picker.minute.toString()

            if (hour == "0") hour = "00"
            if (minute == "0") minute = "00"

            if (title == "Time Start") {
                timeStart = "$hour:$minute"
                binding.btnTimeStart.text = timeStart
            } else {
                timeEnd = "$hour:$minute"
                binding.btnTimeEnd.text = timeEnd
            }
        }
    }

    private fun openDatePicker(binding: FragmentAddEditTaskBinding) {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val materialDatePicker: MaterialDatePicker<Long> =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .setSelection(today)
                .build()

        materialDatePicker.addOnPositiveButtonClickListener {
            val formattedDate = viewModel.formatToDate(it)
            date = formattedDate
            binding.btnDatePicker.text = formattedDate
        }

        materialDatePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
    }


    private fun addCategoryChip(chipText: String, chipGroup: ChipGroup, isChipChecked: Boolean) {
        val chip = Chip(context)
        chip.apply {
            text = chipText
            isCheckable = true
            isChipIconVisible = false
            isCheckedIconVisible = true
            isCloseIconVisible = false
            checkedIcon = context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_mtrl_chip_checked_black
                )
            }
            // TODO(Customise colors later)
            //rippleColor = context?.let { ContextCompat.getColorStateList(it, R.color.mtrl_choice_chip_background_color) }
        }
        chipGroup.addView(chip)
        chip.isChecked = isChipChecked
        chip.isChipIconVisible = isChipChecked
    }
}