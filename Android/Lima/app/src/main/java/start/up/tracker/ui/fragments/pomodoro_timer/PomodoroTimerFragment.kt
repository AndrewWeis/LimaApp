package start.up.tracker.ui.fragments.pomodoro_timer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.databinding.PomodoroTimerFragmentBinding
import start.up.tracker.mvvm.view_models.pomodoro_timer.PomodoroTimerViewModel

@AndroidEntryPoint
class PomodoroTimerFragment : Fragment(R.layout.pomodoro_timer_fragment) {

    private var binding: PomodoroTimerFragmentBinding? = null
    private val viewModel: PomodoroTimerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PomodoroTimerFragmentBinding.bind(view)

        setupTimer()

        setupObservers()
        setupListeners()

        setupButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupTimer() = lifecycleScope.launch {
        viewModel.timer.recoverSavedState()
        viewModel.timer.setupTimerLength()
        viewModel.timer.initCountDownTimer()
    }

    private fun setupObservers() {
        viewModel.timer.timerState.observe(viewLifecycleOwner) { state ->
            updateButtons(state)
        }

        viewModel.timer.secondsRemaining.observe(viewLifecycleOwner) { secondsRemaining ->
            updateCurrentTimeText(secondsRemaining)
        }
    }

    private fun setupListeners() {
        binding?.timerStartButton?.setOnClickListener {
            viewModel.timer.initCountDownTimer()
            viewModel.timer.startTimer()
        }

        binding?.timerPauseButton?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.timer.pauseTimer()
            }
        }

        binding?.timerStopButton?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.timer.stopTimer()
            }
        }

        binding?.timerContinueButton?.setOnClickListener {

            viewModel.timer.initCountDownTimer(viewModel.timer.secondsRemaining.value!!)
            viewModel.timer.startTimer()
        }
    }

    private fun updateCurrentTimeText(secondsRemaining: Long) {
        val minutesUntilFinished = (secondsRemaining / 60).toString()
        val secondsUntilFinished = (secondsRemaining % 60).toString()

        var currentTimeText = "$minutesUntilFinished:$secondsUntilFinished"
        if (secondsUntilFinished.length == 1) {
            currentTimeText = "$minutesUntilFinished:0$secondsUntilFinished"
        }
        binding?.timeText?.text = currentTimeText
    }

    private fun updateButtons(state: Int) {
        when (state) {
            PomodoroTimer.TIMER_STATE_INITIAL, PomodoroTimer.TIMER_STATE_STOPPED -> {
                binding?.timerStartButton?.visibility = View.VISIBLE
                binding?.timerPauseButton?.visibility = View.GONE
                binding?.timerStopButton?.visibility = View.GONE
                binding?.timerContinueButton?.visibility = View.GONE
            }
            PomodoroTimer.TIMER_STATE_RUNNING -> {
                binding?.timerStartButton?.visibility = View.GONE
                binding?.timerPauseButton?.visibility = View.VISIBLE
                binding?.timerStopButton?.visibility = View.GONE
                binding?.timerContinueButton?.visibility = View.GONE
            }
            PomodoroTimer.TIMER_STATE_PAUSED -> {
                binding?.timerStartButton?.visibility = View.GONE
                binding?.timerPauseButton?.visibility = View.GONE
                binding?.timerStopButton?.visibility = View.VISIBLE
                binding?.timerContinueButton?.visibility = View.VISIBLE
            }
        }
    }

    private fun setupButtons() {
        binding?.timerStartButton?.visibility = View.GONE
        binding?.timerPauseButton?.visibility = View.GONE
        binding?.timerStopButton?.visibility = View.GONE
        binding?.timerContinueButton?.visibility = View.GONE
    }
}
