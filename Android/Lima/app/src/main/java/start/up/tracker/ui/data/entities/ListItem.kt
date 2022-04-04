package start.up.tracker.ui.data.entities

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class ListItem(
    var id: String? = null,
    var type: ListItemTypes? = null,
    var data: Any?,
    var settings: Settings = Settings(),
    var error: Error = Error()
)

class Error(var message: String?) {
    constructor() : this(null)
}

class Settings(
    var hint: String? = null,
    var name: String? = null,
    var imeOption: Int? = null,
    var inputType: Int? = null,
    var maxLines: Int? = null,
    var editable: Boolean = true,
    var changeErrorAfterChangingText: Boolean = true,
    @DrawableRes var icon: Int? = null,
    @ColorRes var iconColor: Int? = null,

    var leftMargin: Int? = null,
    var rightMargin: Int? = null,
)
