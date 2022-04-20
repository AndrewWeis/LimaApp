package start.up.tracker.mvvm.view_models.edit_task.dialogs

import androidx.lifecycle.*
import kotlinx.coroutines.flow.combine
import start.up.tracker.utils.screens.StateHandleKeys

class PomodorosViewModel(
    state: SavedStateHandle,
) : ViewModel() {

    private val currentStartTime = state.getLiveData<Int>(StateHandleKeys.CURRENT_START_TIME)
    private val currentPomodoros = state.getLiveData<Int>(StateHandleKeys.CURRENT_POMODOROS)

    val currentEndTime: MutableLiveData<Int> = MutableLiveData()

    val pomodorosData = currentPomodoros.asFlow()
        .combine(currentStartTime.asFlow()) { currentPomodoros, currentStartTime ->
            Pair(currentPomodoros, currentStartTime)
        }.asLiveData()

    fun onIndicatorProgressChanged(currentValue: Int) {
        currentPomodoros.postValue(currentValue)
    }

    fun onTimeStartChanged(time: Int) {
        currentStartTime.postValue(time)
    }

    fun updateEndDate(pomodoros: Int, startTime: Int) {
        val endTime = startTime + pomodoros * 30
        if (pomodoros != -1 && startTime != -1) {
            currentEndTime.postValue(endTime)
        }
    }

    fun clear() {
        currentStartTime.postValue(-1)
        currentEndTime.postValue(-1)
        currentPomodoros.postValue(0)
    }
}
