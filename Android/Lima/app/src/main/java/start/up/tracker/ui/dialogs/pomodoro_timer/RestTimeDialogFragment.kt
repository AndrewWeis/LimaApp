package start.up.tracker.ui.dialogs.pomodoro_timer

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import start.up.tracker.R
import start.up.tracker.databinding.TimerRestTimeDialogFragmentBinding
import start.up.tracker.ui.fragments.pomodoro_timer.PomodoroTimer
import start.up.tracker.utils.screens.ExtraCodes

class RestTimeDialogFragment :
    DialogFragment(R.layout.timer_rest_time_dialog_fragment) {

    private var binding: TimerRestTimeDialogFragmentBinding? = null

    private val args by navArgs<RestTimeDialogFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TimerRestTimeDialogFragmentBinding.bind(view)

        setupListeners()
        setupSelectedRestTime()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupSelectedRestTime() {
        if (args.restTime == PomodoroTimer.POMODORO_REST_SHORT) {
            binding?.rest5SelectionImage?.visibility = View.VISIBLE
            binding?.rest20SelectionImage?.visibility = View.GONE
        } else {
            binding?.rest5SelectionImage?.visibility = View.GONE
            binding?.rest20SelectionImage?.visibility = View.VISIBLE
        }
    }

    private fun setupListeners() {
        binding?.rest5SelectionLayout?.setOnClickListener {
            setFragmentResult(
                requestKey = ExtraCodes.REST_TIME_REQUEST,
                result = bundleOf(ExtraCodes.REST_TIME_REQUEST to PomodoroTimer.POMODORO_REST_SHORT)
            )
            findNavController().popBackStack()
        }

        binding?.rest20SelectionLayout?.setOnClickListener {
            setFragmentResult(
                requestKey = ExtraCodes.REST_TIME_REQUEST,
                result = bundleOf(ExtraCodes.REST_TIME_REQUEST to PomodoroTimer.POMODORO_REST_LONG)
            )
            findNavController().popBackStack()
        }
    }
}
