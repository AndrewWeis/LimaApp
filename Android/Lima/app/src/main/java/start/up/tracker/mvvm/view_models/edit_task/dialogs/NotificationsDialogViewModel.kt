package start.up.tracker.mvvm.view_models.edit_task.dialogs

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import start.up.tracker.entities.NotificationType
import start.up.tracker.utils.screens.StateHandleKeys
import javax.inject.Inject

@HiltViewModel
class NotificationsDialogViewModel @Inject constructor(
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {
    val notificationType = state.getLiveData<NotificationType>(StateHandleKeys.SELECTED_NOTIFICATION_TYPE)

    fun onNotificationClick(selectedNotificationType: NotificationType) {
        notificationType.postValue(selectedNotificationType)
    }
}
