package start.up.tracker.ui.fragments.pomodoro_timer

class PomodoroTimer : Timer() {

    fun setupTimerLength() {
        timerLength = if (iteration % 2 == 0) {
            POMODORO_WORK_TIME
        } else {
            POMODORO_REST_SHORT
        }
    }

    override fun updateSecondsRemaining() {
        setupTimerLength()
        super.updateSecondsRemaining()
    }

    companion object {
        const val POMODORO_WORK_TIME = 6L
        const val POMODORO_REST_SHORT = 3L
        const val POMODORO_REST_LONG = 20L * 60L
    }
}
