package start.up.tracker.ui.dialogs.pomodoro_timer

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import start.up.tracker.R
import start.up.tracker.databinding.TimerModeDialogFragmentBinding
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimerFragment
import start.up.tracker.utils.screens.ExtraCodes

class TimerModeDialogFragment :
    DialogFragment(R.layout.timer_mode_dialog_fragment) {

    private var binding: TimerModeDialogFragmentBinding? = null

    private val args by navArgs<TimerModeDialogFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TimerModeDialogFragmentBinding.bind(view)

        setupListeners()
        setupSelectedTimerMode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupSelectedTimerMode() {
        if (args.mode == PomodoroTimerFragment.THE_CLOSEST_TASK_MODE) {
            binding?.theClosestTaskImage?.visibility = View.VISIBLE
            binding?.freeModeImage?.visibility = View.GONE
        } else {
            binding?.theClosestTaskImage?.visibility = View.GONE
            binding?.freeModeImage?.visibility = View.VISIBLE
        }
    }

    private fun setupListeners() {
        binding?.theClosestTaskLayout?.setOnClickListener {
            setFragmentResult(
                requestKey = ExtraCodes.TIMER_MODE_REQUEST,
                result = bundleOf(ExtraCodes.TIMER_MODE_REQUEST to PomodoroTimerFragment.THE_CLOSEST_TASK_MODE)
            )
            findNavController().popBackStack()
        }

        binding?.freeModeLayout?.setOnClickListener {
            setFragmentResult(
                requestKey = ExtraCodes.TIMER_MODE_REQUEST,
                result = bundleOf(ExtraCodes.TIMER_MODE_REQUEST to PomodoroTimerFragment.FREE_MODE)
            )
            findNavController().popBackStack()
        }
    }
}
