package start.up.tracker.ui.fragments.pomodoro_timer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.pomodoro_timer_fragment.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.database.PreferencesManager
import start.up.tracker.database.TimerDataStore
import start.up.tracker.databinding.PomodoroTimerFragmentBinding
import start.up.tracker.mvvm.view_models.pomodoro_timer.PomodoroTimerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PomodoroTimer : Fragment(R.layout.pomodoro_timer_fragment) {

    private var binding: PomodoroTimerFragmentBinding? = null

    private val viewModel: PomodoroTimerViewModel by viewModels()

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TIMER_STATE_STOPPED

    private var secondsRemaining = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PomodoroTimerFragmentBinding.bind(view)

        initListeners()
    }

    override fun onResume() {
        super.onResume()

        initTimer()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TIMER_STATE_RUNNING) {
            timer.cancel()
        } else if (timerState == TIMER_STATE_PAUSED) {

        }

        // Вот так записывать данные. Далее аналогично
        viewModel.setPreviousTimerLengthSeconds(timerLengthSeconds)
        viewModel.setSecondsRemaining(secondsRemaining)
        viewModel.setTimerState(timerState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initTimer() {
        lifecycleScope.launch {
            timerState = viewModel.getTimerState()

            if (timerState == TIMER_STATE_STOPPED)
                setNewTimerLength()
            else
                setPreviousTimerLength()

            secondsRemaining =
                if (timerState == TIMER_STATE_RUNNING || timerState == TIMER_STATE_PAUSED)
                    viewModel.getSecondsRemaining()
                else
                    timerLengthSeconds

            if (timerState == TIMER_STATE_RUNNING)
                startTimer()

            updateButtons()
            updateCountdownUI()
        }
    }

    private fun onTimerFinished() {
        timerState = TIMER_STATE_STOPPED

        setNewTimerLength()

        lifecycleScope.launch {
            viewModel.setSecondsRemaining(timerLengthSeconds)
        }

        //progress_countdown.progress = 0

        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TIMER_STATE_RUNNING

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = timerLengthPomodoro
        timerLengthSeconds = (lengthInMinutes * 60L)

        //progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        lifecycleScope.launch {
            timerLengthSeconds = viewModel.getPreviousTimerLengthSeconds()
        }
    }

    private fun initListeners() {
        timer_start_button.setOnClickListener {
            startTimer()
            timerState = TIMER_STATE_RUNNING
            updateButtons()
        }

        timer_pause_button.setOnClickListener {
            timer.cancel()
            timerState = TIMER_STATE_PAUSED
            updateButtons()
        }

        timer_stop_button.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondStr = secondsInMinutesUntilFinished.toString()
        textView_countdown.text = "$minutesUntilFinished:${
            if (secondStr.length == 2)
                secondStr
            else
                "0$secondStr"
        }"

        //progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when (timerState) {
            TIMER_STATE_RUNNING -> {
                timer_start_button.isEnabled = false
                timer_pause_button.isEnabled = true
                timer_stop_button.isEnabled = true
            }
            TIMER_STATE_PAUSED -> {
                timer_start_button.isEnabled = true
                timer_pause_button.isEnabled = false
                timer_stop_button.isEnabled = true
            }
            TIMER_STATE_STOPPED -> {
                timer_start_button.isEnabled = true
                timer_pause_button.isEnabled = false
                timer_stop_button.isEnabled = false
            }
        }
    }

    private companion object {
        const val TIMER_STATE_STOPPED = 0
        const val TIMER_STATE_PAUSED = 1
        const val TIMER_STATE_RUNNING = 2

        const val timerLengthPomodoro = 25
        const val timerLengthShortRest = 5
        const val timerLengthLongRest = 5
    }
}