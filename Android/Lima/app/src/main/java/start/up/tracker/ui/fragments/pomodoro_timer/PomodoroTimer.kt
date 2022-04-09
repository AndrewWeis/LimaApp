package start.up.tracker.ui.fragments.pomodoro_timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.first
import start.up.tracker.database.TimerDataStore
import javax.inject.Singleton

@Singleton
class PomodoroTimer(
    private val timerDataStore: TimerDataStore,
) {

    private lateinit var countDownTimer: CountDownTimer

    private val _timerState: MutableLiveData<Int> = MutableLiveData()
    val timerState: LiveData<Int> get() = _timerState

    private val _secondsRemaining: MutableLiveData<Long> = MutableLiveData()
    val secondsRemaining: MutableLiveData<Long> get() = _secondsRemaining

    var restTime = POMODORO_REST_SHORT
    var iteration: Int = 0
    var timerLength: Long = POMODORO_WORK_TIME

    suspend fun recoverSavedState() {
        recoverTimerIteration()
        recoverSecondsRemaining()
        recoverTimerState()
        recoverRestTime()
    }

    suspend fun pauseTimer() {
        countDownTimer.cancel()
        _timerState.postValue(TIMER_STATE_PAUSED)

        saveTimerState()
    }

    suspend fun stopTimer() {
        countDownTimer.cancel()
        _timerState.postValue(TIMER_STATE_STOPPED)
        _secondsRemaining.postValue(timerLength)

        saveTimerState()
    }

    suspend fun handleRestTime(restTime: Long) {
        saveRestTime(restTime)

        this.restTime = restTime

        // if it is rest time
        if (iteration % 2 == 1) {
            setupTimerLength()
        }

        if (timerState.value!! != TIMER_STATE_PAUSED) {
            _secondsRemaining.postValue(timerLength)
        }
    }

    fun startTimer() {
        countDownTimer.start()
        _timerState.postValue(TIMER_STATE_RUNNING)
    }

    fun setupTimerLength() {
        // even - work time, odd - rest time
        timerLength = if (iteration % 2 == 0) {
            POMODORO_WORK_TIME
        } else {
            restTime
        }
    }

    fun initCountDownTimer(initialSeconds: Long = timerLength) {
        // todo (later fix bug when initial seconds = 0)
        countDownTimer = object : CountDownTimer(initialSeconds * SECOND, SECOND) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                _secondsRemaining.postValue(millisUntilFinished / SECOND)
            }
        }
    }

    private suspend fun recoverSecondsRemaining() {
        val savedSecondsRemaining = timerDataStore.secondsRemaining.first()
        _secondsRemaining.postValue(savedSecondsRemaining)
    }

    private suspend fun recoverTimerState() {
        val savedState = timerDataStore.timerState.first()
        _timerState.postValue(savedState)
    }

    private suspend fun saveTimerState() {
        timerDataStore.setTimerIteration(iteration)
        timerDataStore.setSecondsRemaining(secondsRemaining.value!!)
        timerDataStore.setTimerState(_timerState.value!!)
    }

    private suspend fun recoverTimerIteration() {
        val savedIteration = timerDataStore.timerIteration.first()
        iteration = savedIteration
    }

    private suspend fun recoverRestTime() {
        restTime = timerDataStore.timerRestTime.first()
    }

    private suspend fun saveRestTime(restTime: Long) {
        timerDataStore.setRestTime(restTime)
    }

    private fun onTimerFinished() {
        iteration++

        _timerState.postValue(TIMER_STATE_STOPPED)

        setupTimerLength()
        _secondsRemaining.postValue(timerLength)
    }

    companion object {
        const val TIMER_STATE_INITIAL = 0
        const val TIMER_STATE_RUNNING = 1
        const val TIMER_STATE_STOPPED = 2
        const val TIMER_STATE_PAUSED = 3

        const val SECOND = 1000L

        const val POMODORO_WORK_TIME = 6L
        const val POMODORO_REST_SHORT = 2L
        const val POMODORO_REST_LONG = 4L
    }
}
