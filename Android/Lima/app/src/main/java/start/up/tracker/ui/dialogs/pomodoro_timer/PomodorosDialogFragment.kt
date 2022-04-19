package start.up.tracker.ui.dialogs.pomodoro_timer

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import start.up.tracker.R
import start.up.tracker.databinding.PomodorosDialogFragmentBinding
import start.up.tracker.mvvm.view_models.edit_task.dialogs.PomodorosViewModel
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.screens.ExtraCodes
import java.util.*

class PomodorosDialogFragment :
    DialogFragment(R.layout.pomodoros_dialog_fragment),
    TimePickerDialog.OnTimeSetListener {

    private var binding: PomodorosDialogFragmentBinding? = null

    private val viewModel: PomodorosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PomodorosDialogFragmentBinding.bind(view)

        setupObservers()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {
        viewModel.onTimeStartChanged(hours * 60 + minutes)
    }

    private fun setupListeners() {
        binding?.pomodorosIndicator?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
                viewModel.onIndicatorProgressChanged(currentValue)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding?.timeStartButton?.setOnClickListener {
            openTimePicker()
        }

        binding?.doneButton?.setOnClickListener {
            val startTime = viewModel.pomodorosData.value!!.second
            val endTime = viewModel.currentEndTime.value!!
            val pomodoros = viewModel.pomodorosData.value!!.first

            setFragmentResult(
                requestKey = ExtraCodes.POMODORO_REQUEST,
                result = bundleOf(
                    ExtraCodes.POMODORO_START_TIME to startTime,
                    ExtraCodes.POMODORO_END_TIME to endTime,
                    ExtraCodes.POMODORO_POMODOROS to pomodoros,
                )
            )

            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.pomodorosData.observe(viewLifecycleOwner) { data ->
            updateIndicator(data.first)
            updateStartTime(data.second)

            viewModel.updateEndDate(data.first, data.second)
        }

        viewModel.currentEndTime.observe(viewLifecycleOwner) { endTime ->
            updateEndTime(endTime)
        }
    }

    private fun updateEndTime(endTime: Int) {
        binding?.timeEndButton?.text = TimeHelper.formatMinutesOfCurrentDay(endTime)
    }

    private fun updateIndicator(pomodoros: Int) {
        if (pomodoros != -1) {
            binding?.pomodorosIndicator?.progress = pomodoros
            binding?.pomodorosNumberText?.text = pomodoros.toString()
        }
    }

    private fun updateStartTime(startTime: Int) {
        if (startTime != -1) {
            binding?.timeStartButton?.text = TimeHelper.formatMinutesOfCurrentDay(startTime)
        }
    }

    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val hours = calendar[Calendar.HOUR_OF_DAY]
        val minutes = 0

        val timePickerDialog = TimePickerDialog(
            requireContext(), R.style.DatePicker, this,
            hours, minutes, TimeHelper.isSystem24Hour
        )

        timePickerDialog.show()
    }
}
