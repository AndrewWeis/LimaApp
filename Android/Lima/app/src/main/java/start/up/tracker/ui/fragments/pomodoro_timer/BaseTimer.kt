package start.up.tracker.ui.fragments.pomodoro_timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.first
import start.up.tracker.database.TimerDataStore

open class BaseTimer(
    private val timerDataStore: TimerDataStore,
) {
    private val _timerState: MutableLiveData<Int> = MutableLiveData()
    val timerState: LiveData<Int> get() = _timerState

    private val _secondsRemaining: MutableLiveData<Long> = MutableLiveData()
    val secondsRemaining: LiveData<Long> get() = _secondsRemaining

    private val _timerIteration: MutableLiveData<Int> = MutableLiveData()
    val timerIteration: LiveData<Int> get() = _timerIteration

    private lateinit var countDownTimer: CountDownTimer

    var timerLength = DEFAULT_TIMER_LENGTH

    open suspend fun recoverTimerState() {
        _timerIteration.postValue(timerDataStore.timerIteration.first())

        val seconds = timerDataStore.secondsRemaining.first()
        timerLength = seconds

        _secondsRemaining.postValue(seconds)
        _timerState.postValue(timerDataStore.timerState.first())
    }

    open suspend fun saveTimerState() {
        timerDataStore.saveTimerIteration(getTimerIteration())
        timerDataStore.saveSecondsRemaining(getSecondsRemaining())
        timerDataStore.saveTimerState(getTimerState())
    }

    open fun initCountDownTimer() {
        _secondsRemaining.postValue(timerLength)

        countDownTimer = object : CountDownTimer(
            timerLength * SECOND, SECOND
        ) {
            override fun onFinish() {
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {
                _secondsRemaining.postValue(millisUntilFinished / SECOND)
            }
        }
    }

    open fun stopTimer() {
        countDownTimer.cancel()
        _timerState.postValue(TIMER_STATE_STOPPED)
        _secondsRemaining.postValue(DEFAULT_TIMER_LENGTH)
    }

    fun cancelTimer() {
        countDownTimer.cancel()
    }

    fun finish() {
        _timerIteration.postValue(getTimerIteration() + 1)
        _secondsRemaining.postValue(timerLength)
        _timerState.postValue(TIMER_STATE_STOPPED)
    }

    fun startTimer() {
        countDownTimer.start()
        _timerState.postValue(TIMER_STATE_RUNNING)
    }

    fun pauseTimer() {
        countDownTimer.cancel()
        _timerState.postValue(TIMER_STATE_PAUSED)
    }

    fun continueTimer() {
        timerLength = getSecondsRemaining()
        initCountDownTimer()
        startTimer()
    }

    fun restoreRunningState() {
        timerLength = getSecondsRemaining()
        initCountDownTimer()
        startTimer()
    }

    fun setSecondsRemaining(seconds: Long) {
        _secondsRemaining.postValue(seconds)
    }

    fun setIteration(iteration: Int) {
        _timerIteration.postValue(iteration)
    }

    fun setState(state: Int) {
        _timerState.postValue(state)
    }

    fun getTimerIteration(): Int {
        return timerIteration.value!!
    }

    fun getTimerState(): Int {
        return timerState.value!!
    }

    private fun getSecondsRemaining(): Long {
        return secondsRemaining.value!!
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
