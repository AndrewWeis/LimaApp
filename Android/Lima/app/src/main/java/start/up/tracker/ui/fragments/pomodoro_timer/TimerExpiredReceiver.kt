package start.up.tracker.ui.fragments.pomodoro_timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import start.up.tracker.database.TimerDataStore
import start.up.tracker.mvvm.view_models.pomodoro_timer.PomodoroTimerViewModel

class TimerExpiredReceiver constructor(
    private val viewModel: PomodoroTimerViewModel
): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        viewModel.setTimerState(PomodoroTimer.TIMER_STATE_STOPPED)
        viewModel.setAlarmSetTime(0)
    }
}