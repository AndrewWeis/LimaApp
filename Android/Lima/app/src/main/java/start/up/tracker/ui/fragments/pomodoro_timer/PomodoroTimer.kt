package start.up.tracker.ui.fragments.pomodoro_timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.first
import start.up.tracker.database.TimerDataStore
import start.up.tracker.utils.TimeHelper

class PomodoroTimer(
    private val timerDataStore: TimerDataStore
) : BaseTimer(timerDataStore) {

    var restTime = POMODORO_REST_LONG

    private val _timerMode: MutableLiveData<Int> = MutableLiveData()
    val timerMode: LiveData<Int> get() = _timerMode

    private val _timerPhase: MutableLiveData<Int> = MutableLiveData()
    val timerPhase: LiveData<Int> get() = _timerPhase

    var addPomodoro: Boolean = false

    override suspend fun recoverTimerState() {
        super.recoverTimerState()
        _timerMode.value = timerDataStore.timerMode.first()
        restTime = timerDataStore.timerRestTime.first()
        _timerPhase.value = getPhaseFromIteration(getTimerIteration())

        if (getTimerState() == TIMER_STATE_RUNNING || getTimerState() == TIMER_STATE_PAUSED) {
            isFinished = false
        }

        if (getTimerState() == TIMER_STATE_RUNNING) {
            restoreRunningState()
        }
    }

    private fun getPhaseFromIteration(iteration: Int): Int {
        return if (iteration % 2 == 1) REST_PHASE else WORK_PHASE
    }

    override suspend fun stopTimer() {
        super.stopTimer()
        timerLength = getTimerLengthOfPhase()
        setSecondsRemaining(timerLength)
    }

    override suspend fun restoreRunningState() {
        val leftTime = timerDataStore.leftTime.first()
        val currentTime = TimeHelper.getCurrentTimeInMilliseconds() / 1000L
        val difference = currentTime - leftTime

        if (getTimerPhase() == WORK_PHASE) {
            when {
                difference <= getSecondsRemaining() -> {
                    restoreTimerSeconds(0, difference)
                }
                difference <= getSecondsRemaining() + restTime -> {
                    restoreTimerSeconds(restTime, difference)
                    _timerPhase.value = REST_PHASE
                    incrementTimerIteration()
                    incrementPomodoro()
                }
                else -> {
                    resetMode()
                    incrementPomodoro()
                }
            }
        } else if (getTimerPhase() == REST_PHASE) {
            when {
                difference <= getSecondsRemaining() -> {
                    restoreTimerSeconds(0, difference)
                }
                else -> {
                    resetMode()
                }
            }
        }
    }

    override fun getDefaultTimerLength(): Long {
        return POMODORO_WORK_TIME
    }

    suspend fun skipTimer() {
        cancelTimer()
        finish()
    }

    suspend fun handlePhases(iteration: Int) {
        if (isRestPhase(iteration)) {
            _timerPhase.value = REST_PHASE
            startRestPhase()
            startTimer()
            return
        }

        _timerPhase.value = WORK_PHASE
        startWorkPhase()
    }

    fun freeModeWasSelected() {
        timerLength = POMODORO_WORK_TIME
        setSecondsRemaining(timerLength)
        setState(TIMER_STATE_INITIAL)
        setIteration(0)
    }

    fun closestModeWasSelected(completed: Int) {
        setIteration(completed * 2)
        timerLength = getTimerLengthOfPhase()
        setSecondsRemaining(timerLength)
        setState(TIMER_STATE_INITIAL)
    }

    fun isRestPhase(iteration: Int): Boolean {
        return iteration % 2 == 1
    }

    fun setTimerMode(mode: Int) {
        _timerMode.value = mode
    }

    fun getTimerPhase(): Int {
        return timerPhase.value!!
    }

    private fun incrementPomodoro() {
        if (getTimerMode() == CLOSEST_TASK_MODE) {
            addPomodoro = true
        }
    }

    private fun resetMode() {
        if (getTimerMode() == CLOSEST_TASK_MODE) {
            closestModeWasSelected(0)
        }
        if (getTimerMode() == FREE_MODE) {
            freeModeWasSelected()
        }
    }

    private suspend fun restoreTimerSeconds(restTime: Long, diff: Long) {
        timerLength = getSecondsRemaining() + restTime - diff
        initCountDownTimer()
        startTimer()
    }

    private fun getTimerLengthOfPhase(): Long {
        return if (isRestPhase(getTimerIteration())) {
            restTime
        } else {
            POMODORO_WORK_TIME
        }
    }

    private fun startWorkPhase() {
        timerLength = POMODORO_WORK_TIME
        initCountDownTimer()
    }

    private fun startRestPhase() {
        timerLength = restTime
        initCountDownTimer()
    }

    private fun getTimerMode(): Int {
        return timerMode.value!!
    }

    companion object {
        const val POMODORO_WORK_TIME = 12L
        const val POMODORO_REST_SHORT = 4L
        const val POMODORO_REST_LONG = 8L

        const val CLOSEST_TASK_MODE = 1
        const val FREE_MODE = 2

        const val WORK_PHASE = 1
        const val REST_PHASE = 2
    }
}
