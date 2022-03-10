package start.up.tracker.ui.data.entities.forms

data class ListItem(
    var id: String? = null,
    var type: ListItemTypes? = null,
    var data: Any,
    var settings: Settings = Settings(),
    var errors: Errors = Errors()
)

class Errors(var message: String?) {
    constructor() : this(null)
}

class Settings(
    var hint: String? = null,
    var name: String? = null,
    var imeOption: Int? = null,
    var inputType: Int? = null,
)
