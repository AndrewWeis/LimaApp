package start.up.tracker.ui.fragments.pomodoro_timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.first
import start.up.tracker.database.TimerDataStore
import start.up.tracker.utils.TimeHelper

open class BaseTimer(
    private val timerDataStore: TimerDataStore,
) {
    private val _timerState: MutableLiveData<Int> = MutableLiveData()
    val timerState: LiveData<Int> get() = _timerState

    private val _secondsRemaining: MutableLiveData<Long> = MutableLiveData()
    val secondsRemaining: LiveData<Long> get() = _secondsRemaining

    private val _timerIteration: MutableLiveData<Int> = MutableLiveData()
    val timerIteration: LiveData<Int> get() = _timerIteration

    private var countDownTimer: CountDownTimer? = null

    var timerLength = DEFAULT_TIMER_LENGTH
    var isFinished: Boolean = true

    open suspend fun recoverTimerState() {
        _timerIteration.value = timerDataStore.timerIteration.first()

        val seconds = timerDataStore.secondsRemaining.first()
        timerLength = seconds

        _secondsRemaining.value = seconds
        _timerState.value = timerDataStore.timerState.first()
    }

    open suspend fun saveTimerState() {
        timerDataStore.saveTimerIteration(getTimerIteration())
        timerDataStore.saveSecondsRemaining(getSecondsRemaining())
        timerDataStore.saveTimerState(getTimerState())
        timerDataStore.saveLeftTime(TimeHelper.getCurrentTimeInMilliseconds() / 1000L)
    }

    open fun initCountDownTimer() {
        _secondsRemaining.value = timerLength
        countDownTimer = object : CountDownTimer(
            timerLength * SECOND, SECOND
        ) {
            override fun onFinish() {
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {
                _secondsRemaining.value = millisUntilFinished / SECOND
            }
        }
    }

    open fun stopTimer() {
        countDownTimer?.let {
            cancelTimer()
        }

        _timerState.value = TIMER_STATE_STOPPED
        _secondsRemaining.value = DEFAULT_TIMER_LENGTH
    }

    fun cancelTimer() {
        countDownTimer!!.cancel()
        isFinished = true
    }

    fun finish() {
        isFinished = true
        _timerState.value = TIMER_STATE_STOPPED
        _timerIteration.value = getTimerIteration() + 1
        _secondsRemaining.value = timerLength
    }

    fun startTimer() {
        isFinished = false
        countDownTimer!!.start()
        _timerState.value = TIMER_STATE_RUNNING
    }

    fun pauseTimer() {
        cancelTimer()
        _timerState.value = TIMER_STATE_PAUSED
    }

    fun continueTimer() {
        timerLength = getSecondsRemaining()
        initCountDownTimer()
        startTimer()
    }

    open suspend fun restoreRunningState() {
        val leftTime = timerDataStore.leftTime.first()
        val currentTime = TimeHelper.getCurrentTimeInMilliseconds() / 1000L
        val difference = currentTime - leftTime

        timerLength = if (leftTime != 0L || difference <= getSecondsRemaining()) {
            getSecondsRemaining() - difference
        } else {
            getSecondsRemaining()
        }

        initCountDownTimer()
        startTimer()
    }

    fun setSecondsRemaining(seconds: Long) {
        _secondsRemaining.value = seconds
    }

    fun setIteration(iteration: Int) {
        _timerIteration.value = iteration
    }

    fun setState(state: Int) {
        _timerState.value = state
    }

    fun getTimerIteration(): Int {
        return timerIteration.value!!
    }

    fun getTimerState(): Int {
        return timerState.value!!
    }

    fun getSecondsRemaining(): Long {
        return secondsRemaining.value!!
    }

    protected fun incrementTimerIteration() {
        _timerIteration.value = getTimerIteration() + 1
    }

    companion object {
        const val TIMER_STATE_INITIAL = 0
        const val TIMER_STATE_RUNNING = 1
        const val TIMER_STATE_STOPPED = 2
        const val TIMER_STATE_PAUSED = 3

        const val SECOND = 1000L

        const val DEFAULT_TIMER_LENGTH = 6L
    }
}
