package start.up.tracker.ui.list.generators.settings

import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.settings.Setting
import start.up.tracker.ui.list.generators.base.BaseGenerator

class SettingsGenerator : BaseGenerator() {

    fun createListItems(settings: List<Setting>): List<ListItem> {
        return settings.map { setting ->
            val listItem = createListItem(
                id = null,
                type = null,
                data = setting,
            )
            listItem
        }
    }
}
