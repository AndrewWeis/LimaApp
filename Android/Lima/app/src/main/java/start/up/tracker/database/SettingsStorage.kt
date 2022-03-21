package start.up.tracker.database

import start.up.tracker.R
import start.up.tracker.ui.data.entities.settings.Setting
import start.up.tracker.utils.resources.ResourcesUtils

object SettingsStorage {

    fun getSettings(): List<Setting> {
        val settings = mutableListOf<Setting>()

        settings.add(
            Setting(
                id = 1,
                imageRes = R.drawable.img_app_appearance,
                title = ResourcesUtils.getString(R.string.appearance_text)
            )
        )

        settings.add(
            Setting(
                id = 2,
                imageRes = R.drawable.img_time_management_techniques,
                title = ResourcesUtils.getString(R.string.techniques_text)
            )
        )

        return settings.toList()
    }
}
