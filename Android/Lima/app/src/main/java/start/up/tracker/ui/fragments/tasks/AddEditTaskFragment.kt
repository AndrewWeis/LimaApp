package start.up.tracker.ui.fragments.tasks

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
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
import start.up.tracker.mvvm.view_models.tasks.AddEditTaskViewModel
import start.up.tracker.utils.exhaustive
import start.up.tracker.utils.timeToMinutes
import kotlin.properties.Delegates.notNull

// todo (RECODE WHOLE F*CKING CLASS. IT LOOKS LIKE SHIT!!!)

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    private var binding: FragmentAddEditTaskBinding? = null

    private var date by notNull<String>()
    private var dateLong by notNull<Long>()
    private var timeStart by notNull<String>()
    private var timeEnd by notNull<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditTaskBinding.bind(view)

        initVariables()
        initTextListeners()
        initListeners()
        initTaskEventListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initTaskEventListener() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.addEditTaskEvent.collect { event ->
            when (event) {
                is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                    binding?.editTextTaskLabel?.clearFocus()
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

    private fun initListeners() {
        binding?.fabSaveTask?.setOnClickListener {

            if (!isDataValid()) {
                return@setOnClickListener
            }

            // todo (Think how to separate. Recode.)
            val checkedChip = binding?.chipCategoriesGroup?.children
                ?.toList()
                ?.filter { (it as Chip).isChecked }
                ?.joinToString { (it as Chip).text }

            val checkedPriority = binding?.chipPriorityGroup?.children
                ?.toList()
                ?.filter { (it as Chip).isChecked }
                ?.joinToString { (it as Chip).text }

            val priority = viewModel.priorityToInt(checkedPriority!!)

            viewModel.onSaveClick(checkedChip!!, date, dateLong, timeStart, timeEnd, priority)
        }

        binding?.btnDatePicker?.setOnClickListener {
            openDatePicker()
        }

        binding?.btnTimeStart?.setOnClickListener {
            openTimePicker("Time Start")
        }

        binding?.btnTimeEnd?.setOnClickListener {
            openTimePicker("Time End")
        }

        binding?.btnClear?.setOnClickListener {
            date = "No date"
            dateLong = 0
            timeStart = "No time"
            timeEnd = "No time"

            binding?.apply {
                btnDatePicker.text = date
                btnTimeStart.text = timeStart
                btnTimeEnd.text = timeEnd
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
                if (subset.categoryId == category.categoryId) {
                    isChipChecked = true
                }
                addCategoryChip(category.categoryName, binding!!.chipCategoriesGroup, isChipChecked)
            }
        }
    }

    // todo (Move to separate class for validation logic)
    private fun isDataValid(): Boolean {
        if ((timeStart != "No time" && timeEnd == "No time") || (timeStart == "No time" && timeEnd != "No time")) {
            Snackbar.make(
                requireView(),
                "You must specify both time intervals",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }

        if (timeStart != "No time" && timeEnd != "No time" && timeToMinutes(timeEnd) - timeToMinutes(
                timeStart
            ) < 30
        ) {
            Snackbar.make(
                requireView(),
                "The minimum time interval must be >= 30",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }

        if (timeStart != "No time" && timeEnd != "No time" && date == "No date") {
            Snackbar.make(
                requireView(),
                "You must specify date if you choose time interval",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }
        // TODO(check if there are already some task in that time interval)
        return true
    }

    private fun initTextListeners() {
        binding?.editTextTaskLabel?.addTextChangedListener {
            viewModel.taskName = it.toString()
        }

        binding?.descriptionET?.addTextChangedListener {
            viewModel.taskDesc = it.toString()
        }
    }

    // todo (wrong use of ViewModel. Recode.)
    private fun initVariables() {
        date = viewModel.taskDate
        dateLong = viewModel.taskDateLong
        timeStart = viewModel.taskTimeStart
        timeEnd = viewModel.taskTimeEnd

        binding?.apply {
            btnDatePicker.text = viewModel.taskDate
            btnTimeStart.text = viewModel.taskTimeStart
            btnTimeEnd.text = viewModel.taskTimeEnd

            editTextTaskLabel.setText(viewModel.taskName)
            descriptionET.setText(viewModel.taskDesc)

            chipPriorityGroup.isSingleSelection = true
            chipPriorityGroup.isSelectionRequired = true
            chipPriorityGroup.forEach {
                val textPriority = (it as Chip).text.toString()
                val priority = viewModel.priorityToInt(textPriority)
                if (viewModel.taskPriority == priority) {
                    it.isChecked = true
                }
            }
        }
    }

    private fun openTimePicker(title: String) {
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
                binding?.btnTimeStart?.text = timeStart
            } else {
                timeEnd = "$hour:$minute"
                binding?.btnTimeEnd?.text = timeEnd
            }
        }
    }

    private fun openDatePicker() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val materialDatePicker: MaterialDatePicker<Long> =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .setSelection(today)
                .build()

        materialDatePicker.addOnPositiveButtonClickListener {
            dateLong = it
            val formattedDate = viewModel.formatToDate(it)
            date = formattedDate
            binding?.btnDatePicker?.text = formattedDate
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
