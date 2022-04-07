package start.up.tracker.ui.fragments.pomodoro_timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.pomodoro_timer_fragment.*
import kotlinx.coroutines.launch
import start.up.tracker.R
import start.up.tracker.databinding.PomodoroTimerFragmentBinding
import start.up.tracker.entities.Task
import start.up.tracker.mvvm.view_models.pomodoro_timer.PomodoroTimerViewModel
import start.up.tracker.utils.TimeHelper
import java.util.*

@AndroidEntryPoint
class PomodoroTimer : Fragment(R.layout.pomodoro_timer_fragment) {

    private var binding: PomodoroTimerFragmentBinding? = null
    private val viewModel: PomodoroTimerViewModel by viewModels()
    private lateinit var timer: CountDownTimer

    private var timerLengthSeconds = 0L
    private var secondsRemaining = 0L

    private var timerState = TIMER_STATE_DISABLED
    private var currentPhase = CURRENT_PHASE_POMODORO
    private var pomodoroNumber = 4
    private var currentPomodoro = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PomodoroTimerFragmentBinding.bind(view)

        initListeners()

        lifecycleScope.launch {
            val taskToMatch = viewModel.findTaskToMatch()

            if (taskToMatch != null) {
                taskMatchedCase(taskToMatch)
            } else {
                taskNotMatchedCase()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TIMER_STATE_RUNNING) {
            timer.cancel()
            val wakeUpTime = setAlarm(nowSeconds, secondsRemaining)
        } else if (timerState == TIMER_STATE_PAUSED) {

        }

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

            currentPhaseText()

            if (timerState == TIMER_STATE_STOPPED)
                setNewTimerLength()
            else
                setPreviousTimerLength()

            secondsRemaining =
                if (timerState == TIMER_STATE_RUNNING || timerState == TIMER_STATE_PAUSED)
                    viewModel.getSecondsRemaining()
                else
                    timerLengthSeconds

            val alarmSetTime = viewModel.getAlarmSetTime()
            if (alarmSetTime > 0)
                secondsRemaining -= nowSeconds - alarmSetTime

            if (secondsRemaining <= 0)
                onTimerFinished(false)
            else if (timerState == TIMER_STATE_RUNNING)
                startTimer()

            updateButtons()
            updateCountDownTimeText()
        }
    }

    private fun onTimerFinished(byForce: Boolean) {
        timerState = TIMER_STATE_STOPPED

        if (!byForce) {
            if (currentPhase == CURRENT_PHASE_POMODORO && currentPomodoro < pomodoroNumber) {
                currentPhase = CURRENT_PHASE_SHORT_REST
                currentPomodoro++
            } else if (currentPhase == CURRENT_PHASE_POMODORO && currentPomodoro == pomodoroNumber) {
                currentPhase = CURRENT_PHASE_LONG_REST
                currentPomodoro = 1
            } else if (currentPhase == CURRENT_PHASE_SHORT_REST) {
                currentPhase = CURRENT_PHASE_POMODORO
            } else if (currentPhase == CURRENT_PHASE_LONG_REST) {
                currentPhase = CURRENT_PHASE_POMODORO
            }
        }

        currentPhaseText()
        setNewTimerLength()

        lifecycleScope.launch {
            viewModel.setSecondsRemaining(timerLengthSeconds)
        }

        //progress_countdown.progress = 0

        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountDownTimeText()
    }

    private fun startTimer() {
        timerState = TIMER_STATE_RUNNING

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished(false)

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountDownTimeText()
            }
        }.start()
    }

    private fun currentPhaseText() {
        when (currentPhase) {
            CURRENT_PHASE_POMODORO -> {
                binding?.currentPhaseText?.text = "Pomodoro phase"
            }
            CURRENT_PHASE_SHORT_REST -> {
                binding?.currentPhaseText?.text = "Short rest phase"
            }
            else -> {
                binding?.currentPhaseText?.text = "Long rest phase"
            }
        }
    }

    private fun setNewTimerLength() {
        val lengthInMinutes: Int = when (currentPhase) {
            CURRENT_PHASE_POMODORO -> {
                LENGTH_POMODORO
            }
            CURRENT_PHASE_SHORT_REST -> {
                LENGTH_SHORT_REST
            }
            else -> {
                LENGTH_LONG_REST
            }
        }

        timerLengthSeconds = (lengthInMinutes * 60L)
        //progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        lifecycleScope.launch {
            timerLengthSeconds = viewModel.getPreviousTimerLengthSeconds()
        }
    }

    private fun initListeners() {
        binding?.timerStartButton?.setOnClickListener {
            startTimer()
            timerState = TIMER_STATE_RUNNING
            updateButtons()
        }

        binding?.timerPauseButton?.setOnClickListener {
            timer.cancel()
            timerState = TIMER_STATE_PAUSED
            updateButtons()
        }

        binding?.timerStopButton?.setOnClickListener {
            timer.cancel()
            onTimerFinished(true)
        }
    }

    private fun updateCountDownTimeText() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondStr = secondsInMinutesUntilFinished.toString()
        binding?.timeText?.text = "$minutesUntilFinished:${
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
            TIMER_STATE_DISABLED -> {
                timer_start_button.isEnabled = false
                timer_pause_button.isEnabled = false
                timer_stop_button.isEnabled = false
            }
        }
    }

    /**
     * Нужно сделать плашку с автоматически выбранной таской с числом помодорок у этой таски
     * Также крестик, по нажатию на который мы прервём привязку к таске. В таком случае вызывается
     * taskNotMatchedCase. Старт таймера доступен сразу
     */
    private fun taskMatchedCase(taskToMatch: Task) {
        pomodoroNumber = viewModel.fromEndTimeToPomodoro(taskToMatch)!!

        // TODO Отрисовать плашку с автоматически выбранной активностью с числом помодорок у этой таски
        // предварительно
        // TODO
        binding?.taskFoundText?.text = "${taskToMatch.taskTitle}"
        // TODO
        binding?.startTimeText?.text = "${TimeHelper.formatMinutesOfCurrentDay(taskToMatch.startTimeInMinutes)}"
        // TODO
        binding?.pomodorosText?.text = "$pomodoroNumber"

        timerState = TIMER_STATE_STOPPED
        updateButtons()
    }

    /**
     * Нужно сделать плашку с выбором число помодорок. Старт таймера доступен сразу после выставления
     * помодорок (из состояние disabled в stopped)
     */
    private fun taskNotMatchedCase() {
        // временно для дебага
        binding?.taskFoundText?.text = "no task found"

        // пока так
        pomodoroNumber = 3

        binding?.pomodorosText?.text = "$pomodoroNumber"

        // TODO какая-то кнопочка для выставления помодорок. Не выставив помодорки, нельзя нажимать кнопочки старта таймера
        /*timer_..._button.setOnClickListener {
            timerState = TIMER_STATE_PAUSED
            updateButtons()
        }*/
    }

    private fun setAlarm(nowSeconds: Long, secondsRemaining: Long): Long {
        val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
        val context = requireContext()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerExpiredReceiver(viewModel)::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
        viewModel.setAlarmSetTime(nowSeconds)

        return wakeUpTime
    }

    private fun removeAlarm() {
        val context = requireContext()
        val intent = Intent(context, TimerExpiredReceiver(viewModel)::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        viewModel.setAlarmSetTime(0)
    }

    private val nowSeconds: Long
        get() = Calendar.getInstance().timeInMillis / 1000

    companion object {

        const val TIMER_STATE_STOPPED = 0
        const val TIMER_STATE_PAUSED = 1
        const val TIMER_STATE_RUNNING = 2
        const val TIMER_STATE_DISABLED = 3

        const val CURRENT_PHASE_POMODORO = 4
        const val CURRENT_PHASE_SHORT_REST = 5
        const val CURRENT_PHASE_LONG_REST = 6

        const val LENGTH_POMODORO = 25
        const val LENGTH_SHORT_REST = 5
        const val LENGTH_LONG_REST = 20
    }
}
