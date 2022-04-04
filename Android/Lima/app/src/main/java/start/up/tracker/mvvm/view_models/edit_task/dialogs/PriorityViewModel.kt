package start.up.tracker.mvvm.view_models.edit_task.dialogs

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class PriorityViewModel @Inject constructor(
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    val priorityId = state.getLiveData<Int>(StateHandleKeys.SELECTED_PRIORITY_ID)
    val principleId = state.get<Int>(StateHandleKeys.PRINCIPLE_ID)

    fun onPriorityClick(selectedPriorityId: Int) {
        priorityId.postValue(selectedPriorityId)
    }
}
