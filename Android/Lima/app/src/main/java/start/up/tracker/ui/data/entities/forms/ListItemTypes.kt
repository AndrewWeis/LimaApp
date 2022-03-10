package start.up.tracker.ui.data.entities.forms

/**
 * Типы ListItem
 */
enum class ListItemTypes {
    // ListItem с переходом (подразумевается, что поле заполнятся)
    SELECT,

    // ListItem с checkbox
    CHECKBOX,

    // ListItem с RadioButton
    RADIO_BUTTON,

    // ListItem c RadioGroup
    RADIO_GROUP,

    // ListItem с возможностью перехода (без выбора)
    LINK,

    // ListItem пока с любой кнопкой, возможно, кнопки будут делиться по типам
    BUTTON,

    // ListItem и заголовком блока на экране, возможно, будут разделения по типам
    HEADER,

    // ListItem со списком: например, галлерея фотографий
    LIST,

    // ListItem с тестовым сообщением
    TEXT,

    // ListItem с возможностью ввести значение
    INPUT_TEXT,

    // ListItem выбор значения без перехода (Spinner)
    INLINE_SELECT,

    // ListItem с Switch элементом
    SWITCH,
}
