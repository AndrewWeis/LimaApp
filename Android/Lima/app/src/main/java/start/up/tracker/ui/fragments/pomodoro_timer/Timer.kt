package start.up.tracker.ui.fragments.pomodoro_timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class Timer {

    private lateinit var countDownTimer: CountDownTimer

    private val _timerState: MutableLiveData<Int> = MutableLiveData()
    val timerState: LiveData<Int> get() = _timerState

    private val _secondsRemaining: MutableLiveData<Long> = MutableLiveData()
    val secondsRemaining: MutableLiveData<Long> get() = _secondsRemaining

    var iteration: Int = 0
    var timerLength: Long = DEFAULT_TIME_IN_SECONDS

    open fun updateSecondsRemaining() {
        _secondsRemaining.postValue(timerLength)
    }

    fun recoverTimerState(state: Int) {
        _timerState.postValue(state)
    }

    fun recoverTimerIteration(savedIteration: Int) {
        iteration = savedIteration
    }

    fun startTimer() {
        countDownTimer.start()
        _timerState.postValue(TIMER_STATE_RUNNING)
    }

    fun pauseTimer() {
        countDownTimer.cancel()
        _timerState.postValue(TIMER_STATE_PAUSED)
    }

    fun stopTimer() {
        countDownTimer.cancel()
        _timerState.postValue(TIMER_STATE_STOPPED)
    }

    fun initCountDownTimer(initialSeconds: Long) {
        countDownTimer = object : CountDownTimer(initialSeconds * SECOND, SECOND) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                _secondsRemaining.postValue(millisUntilFinished / SECOND)
            }
        }
    }

    private fun onTimerFinished() {
        iteration++
        _timerState.postValue(TIMER_STATE_STOPPED)
        _secondsRemaining.postValue(timerLength)
    }

    companion object {
        const val TIMER_STATE_INITIAL = 0
        const val TIMER_STATE_RUNNING = 1
        const val TIMER_STATE_STOPPED = 2
        const val TIMER_STATE_PAUSED = 3

        const val SECOND = 1000L
        const val DEFAULT_TIME_IN_SECONDS = 60L * 60L
    }
}
