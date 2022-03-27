package start.up.tracker.ui.list.generators.techniques

import start.up.tracker.entities.Technique
import start.up.tracker.ui.data.constants.ListItemIds
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.list.generators.base.BaseGenerator

class TechniqueGenerator : BaseGenerator() {

    /**
     * Получить список [ListItem]'ов
     *
     * @param techniques список техник
     * @return список [ListItem]'ов
     */
    fun createListItems(techniques: List<Technique>?): List<ListItem> {
        if (techniques.isNullOrEmpty()) {
            return listOf()
        }

        val list: MutableList<ListItem> = mutableListOf()

        techniques.forEach { technique ->
            list.add(getTechniquesListItem(technique))
        }

        return list
    }

    /**
     * Получить listItem с техникой тайм менеджмента
     *
     * @param technique техника
     * @return listItem с техникой тайм менеджмента
     */
    private fun getTechniquesListItem(technique: Technique): ListItem {
        return createListItem(
            id = ListItemIds.TIME_MANAGEMENT_TECHNIQUE,
            type = ListItemTypes.TECHNIQUE,
            data = technique
        )
    }
}
