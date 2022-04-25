package start.up.tracker.mvvm.view_models.edit_task.dialogs

import androidx.hilt.Assisted
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class NotificationsDialogViewModel @Inject constructor(
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {
    val notificationId = state.getLiveData<Int>(StateHandleKeys.SELECTED_NOTIFICATION_ID)

    fun onNotificationClick(selectedPriorityId: Int) {
        notificationId.postValue(selectedPriorityId)
    }
}