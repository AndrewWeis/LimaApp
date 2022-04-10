package start.up.tracker.ui.fragments.pomodoro_timer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.databinding.PomodoroTimerFragmentBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.pomodoro_timer.PomodoroTimerViewModel
import start.up.tracker.mvvm.view_models.pomodoro_timer.PomodoroTimerViewModel.TimerEvent
import start.up.tracker.ui.fragments.base.BaseNavigationFragment
import start.up.tracker.utils.screens.ExtraCodes

@AndroidEntryPoint
class PomodoroTimerFragment :
    BaseNavigationFragment(R.layout.pomodoro_timer_fragment) {

    private var binding: PomodoroTimerFragmentBinding? = null
    private val viewModel: PomodoroTimerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PomodoroTimerFragmentBinding.bind(view)

        setupTimer()

        setupObservers()
        setupListeners()
        setupEventsListeners()
        setupResultListeners()

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

        viewModel.timerMode.observe(viewLifecycleOwner) { mode ->
            updateTimerMode(mode)
        }

        viewModel.closestTask.observe(viewLifecycleOwner) { task ->
            task?.let { showClosestTask(it) } ?: showClosestTasksNotFoundMessage()
        }

        viewModel.timer.timerIteration.observe(viewLifecycleOwner) { iteration ->
            if (iteration % 2 == 1) { updateTaskPomodoros() }
        }
    }

    private fun updateTaskPomodoros() {
        val completedPomodoros = viewModel.closestTask.value?.completedPomodoros!! + 1
        val totalPomodoros = viewModel.closestTask.value?.pomodoros
        val pomodorosText = "$completedPomodoros / $totalPomodoros"

        binding?.task?.closestTaskPomodorosText?.text = pomodorosText
        viewModel.updateCompletedPomodoros(completedPomodoros)
    }

    private fun updateTimerMode(mode: Int) {
        when (mode) {
            CLOSEST_TASK_MODE -> { viewModel.onClosestTaskModeSelected() }
            FREE_MODE -> {}
        }
    }

    private fun showClosestTasksNotFoundMessage() {
        binding?.task?.closestTaskTitleText?.text = "No task on today"
    }

    private fun showClosestTask(task: Task) {
        binding?.task?.closestTaskTitleText?.text = task.taskTitle

        val pomodorosText = "${task.completedPomodoros}/${task.pomodoros}"
        binding?.task?.closestTaskPomodorosText?.text = pomodorosText
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

        binding?.timerRestTimeButton?.setOnClickListener {
            viewModel.onRestTimeButtonClick()
        }

        binding?.timerModeButton?.setOnClickListener {
            viewModel.onModeButtonClick()
        }
    }

    private fun setupEventsListeners() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.timerEvents.collect { event ->
            when (event) {
                is TimerEvent.NavigateToRestTimeDialog -> {
                    val action = PomodoroTimerFragmentDirections.actionPomodoroTimerToRestTime(
                        restTime = event.restTime
                    )
                    navigateTo(action)
                }

                is TimerEvent.NavigateToModeDialog -> {
                    val action = PomodoroTimerFragmentDirections.actionPomodoroTimerToTimerMode(
                        mode = event.mode
                    )
                    navigateTo(action)
                }
            }
        }
    }

    private fun setupResultListeners() {
        setFragmentResultListener(ExtraCodes.REST_TIME_REQUEST) { requestKey, bundle ->
            val restTime = bundle.getLong(requestKey)
            lifecycleScope.launch {
                viewModel.timer.handleRestTime(restTime)
            }
        }

        setFragmentResultListener(ExtraCodes.TIMER_MODE_REQUEST) { requestKey, bundle ->
            val timerMode = bundle.getInt(requestKey)
            viewModel.handleTimerMode(timerMode)
        }
    }

    companion object {
        const val CLOSEST_TASK_MODE = 1
        const val FREE_MODE = 2
    }
}
