package start.up.tracker.database

import start.up.tracker.R
import start.up.tracker.ui.data.entities.settings.Setting
import start.up.tracker.utils.resources.ResourcesUtils

class SettingsStorage {

    fun getSettings(): List<Setting> {
        val settings = mutableListOf<Setting>()

        settings.add(
            Setting(
                id = SETTING_APPEARANCE_ID,
                imageRes = R.drawable.img_app_appearance,
                title = ResourcesUtils.getString(R.string.appearance_text)
            )
        )

        settings.add(
            Setting(
                id = SETTING_TECHNIQUES_ID,
                imageRes = R.drawable.img_time_management_techniques,
                title = ResourcesUtils.getString(R.string.techniques_text)
            )
        )

        return settings.toList()
    }

    companion object SettingIds {
        const val SETTING_APPEARANCE_ID = 1
        const val SETTING_TECHNIQUES_ID = 2
    }
}
