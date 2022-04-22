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
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimer
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
        if (args.mode == PomodoroTimer.CLOSEST_TASK_MODE) {
            binding?.closestTaskSelectedImage?.visibility = View.VISIBLE
            binding?.freeModeSelectedImage?.visibility = View.GONE
        } else {
            binding?.closestTaskSelectedImage?.visibility = View.GONE
            binding?.freeModeSelectedImage?.visibility = View.VISIBLE
        }
    }

    private fun setupListeners() {
        binding?.closestTaskLayout?.setOnClickListener {
            setFragmentResult(
                requestKey = ExtraCodes.TIMER_MODE_REQUEST,
                result = bundleOf(ExtraCodes.TIMER_MODE_REQUEST to PomodoroTimer.CLOSEST_TASK_MODE)
            )
            findNavController().popBackStack()
        }

        binding?.freeModeLayout?.setOnClickListener {
            setFragmentResult(
                requestKey = ExtraCodes.TIMER_MODE_REQUEST,
                result = bundleOf(ExtraCodes.TIMER_MODE_REQUEST to PomodoroTimer.FREE_MODE)
            )
            findNavController().popBackStack()
        }
    }
}
