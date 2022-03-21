package start.up.tracker.mvvm.view_models.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import start.up.tracker.database.SettingsStorage.SettingIds.SETTING_APPEARANCE_ID
import start.up.tracker.database.SettingsStorage.SettingIds.SETTING_TECHNIQUES_ID
import start.up.tracker.ui.data.entities.settings.Setting

class SettingsViewModel : ViewModel() {

    private val settingsEventChannel = Channel<SettingEvent>()
    val settingsEvent = settingsEventChannel.receiveAsFlow()

    fun onSettingClick(setting: Setting) = viewModelScope.launch {
        when (setting.id) {
            SETTING_APPEARANCE_ID -> settingsEventChannel.send(SettingEvent.NavigateToAppearance)
            SETTING_TECHNIQUES_ID -> settingsEventChannel.send(SettingEvent.NavigateToTechniques)
        }
    }

    sealed class SettingEvent {
        object NavigateToAppearance : SettingEvent()
        object NavigateToTechniques : SettingEvent()
    }
}
