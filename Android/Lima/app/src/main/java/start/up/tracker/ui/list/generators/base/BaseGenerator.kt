package start.up.tracker.ui.list.generators.base

import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.ListItemTypes
import start.up.tracker.ui.data.entities.forms.Settings

open class BaseGenerator {

    /**
     * Создает [элемент списка][ListItem]
     *
     * @param id        идентификатор элемента
     * @param type      тип элемента
     * @param data      значение
     * @param settings  настройки
     * @return элемент списка
     */
    protected fun createListItem(id: String, type: ListItemTypes, data: Any?, settings: Settings): ListItem {
        val listItem: ListItem = createListItem(id, type, data)
        return listItem.copy(settings = settings)
    }

    /**
     * Создает [элемент списка][ListItem]
     *
     * @param id        идентификатор элемента
     * @param type      тип элемента
     * @param data     значение
     * @return элемент списка
     */
    protected fun createListItem(id: String, type: ListItemTypes, data: Any?): ListItem {
        return ListItem(
            id = id,
            type = type,
            data = data
        )
    }
}
