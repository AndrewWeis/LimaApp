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

    private val timer = PomodoroTimer()

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
        val savedIteration = viewModel.getTimerIteration()
        timer.recoverTimerIteration(savedIteration)
        timer.setupTimerLength()

        val savedSecondsRemaining = viewModel.getSecondsRemaining()
        updateCurrentTimeText(savedSecondsRemaining)

        timer.initCountDownTimer(savedSecondsRemaining)

        val savedState = viewModel.getTimerState()
        timer.recoverTimerState(savedState)
    }

    private fun setupObservers() {
        timer.timerState.observe(viewLifecycleOwner) { state ->
            viewModel.saveTimerState(state)
            updateButtons(state)
            updateSecondsRemaining(state)
        }

        timer.secondsRemaining.observe(viewLifecycleOwner) { secondsRemaining ->
            updateCurrentTimeText(secondsRemaining)
        }
    }

    private fun setupListeners() {
        binding?.timerStartButton?.setOnClickListener {
            timer.initCountDownTimer(timer.timerLength)
            timer.startTimer()
        }

        binding?.timerPauseButton?.setOnClickListener {
            timer.pauseTimer()
            viewModel.saveSecondsRemaining(getSecondsRemaining())
            viewModel.saveIteration(timer.iteration)
        }

        binding?.timerStopButton?.setOnClickListener {
            timer.stopTimer()
            viewModel.saveSecondsRemaining(timer.timerLength)
            viewModel.saveIteration(timer.iteration)
            updateCurrentTimeText(timer.timerLength)
        }

        binding?.timerContinueButton?.setOnClickListener {
            timer.initCountDownTimer(getSecondsRemaining())
            timer.startTimer()
        }
    }

    private fun updateSecondsRemaining(state: Int) {
        if (state == Timer.TIMER_STATE_STOPPED) {
            timer.updateSecondsRemaining()
        }
    }

    private fun getSecondsRemaining(): Long {
        var secondsRemaining: Long? = timer.secondsRemaining.value
        if (secondsRemaining == null) {
            lifecycleScope.launch {
                secondsRemaining = viewModel.getSecondsRemaining()
            }
        }
        return secondsRemaining!!
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
            Timer.TIMER_STATE_INITIAL, Timer.TIMER_STATE_STOPPED -> {
                binding?.timerStartButton?.visibility = View.VISIBLE
                binding?.timerPauseButton?.visibility = View.GONE
                binding?.timerStopButton?.visibility = View.GONE
                binding?.timerContinueButton?.visibility = View.GONE
            }
            Timer.TIMER_STATE_RUNNING -> {
                binding?.timerStartButton?.visibility = View.GONE
                binding?.timerPauseButton?.visibility = View.VISIBLE
                binding?.timerStopButton?.visibility = View.GONE
                binding?.timerContinueButton?.visibility = View.GONE
            }
            Timer.TIMER_STATE_PAUSED -> {
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
