package start.up.tracker.ui.fragments.pomodoro_timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.first
import start.up.tracker.database.TimerDataStore
import start.up.tracker.entities.Task

class PomodoroTimer(
    private val timerDataStore: TimerDataStore
) : BaseTimer(timerDataStore) {

    var restTime = POMODORO_REST_LONG

    private val _timerMode: MutableLiveData<Int> = MutableLiveData()
    val timerMode: LiveData<Int> get() = _timerMode

    private val _timerPhase: MutableLiveData<Int> = MutableLiveData()
    val timerPhase: LiveData<Int> get() = _timerPhase

    override suspend fun recoverTimerState() {
        super.recoverTimerState()
        _timerMode.postValue(timerDataStore.timerMode.first())
        restTime = timerDataStore.timerRestTime.first()
    }

    override fun stopTimer() {
        super.stopTimer()
        timerLength = getTimerLengthOfPhase()
        setSecondsRemaining(timerLength)
    }

    fun skipTimer() {
        cancelTimer()
        finish()
    }

    fun handlePhases(iteration: Int) {
        if (isRestPhase(iteration)) {
            _timerPhase.postValue(REST_PHASE)
            startRestPhase()
            startTimer()
            return
        }

        _timerPhase.postValue(WORK_PHASE)
        startWorkPhase()
    }

    fun freeModeWasSelected() {
        timerLength = POMODORO_WORK_TIME
        setSecondsRemaining(timerLength)
        setState(TIMER_STATE_INITIAL)
        setIteration(0)
    }

    fun closestModeWasSelected(task: Task) {
        setIteration(task.completedPomodoros!! * 2)
        timerLength = getTimerLengthOfPhase()
        setSecondsRemaining(timerLength)
        setState(TIMER_STATE_INITIAL)
    }

    fun isRestPhase(iteration: Int): Boolean {
        return iteration % 2 == 1
    }

    fun setTimerMode(mode: Int) {
        _timerMode.postValue(mode)
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

    companion object {
        const val POMODORO_WORK_TIME = 6L
        const val POMODORO_REST_SHORT = 2L
        const val POMODORO_REST_LONG = 4L

        const val CLOSEST_TASK_MODE = 1
        const val FREE_MODE = 2

        const val WORK_PHASE = 1
        const val REST_PHASE = 2
    }
}
