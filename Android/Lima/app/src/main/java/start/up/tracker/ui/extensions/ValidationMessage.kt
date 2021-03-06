package start.up.tracker.ui.extensions

import start.up.tracker.R
import start.up.tracker.data.fields.Field
import start.up.tracker.data.fields.validations.RequiredValidation
import start.up.tracker.data.fields.validations.UnknownReasonValidation
import start.up.tracker.utils.resources.ResourcesUtils

/**
 * Генерация текста ошибок для валидации
 */
class ValidationMessages(private var field: Field<*>) {

    fun getMessage(): String? {
        if (field.isValid()) {
            return null
        }

        val unknownReasonValidation = field.findInvalidValidation(
            UnknownReasonValidation::class.java
        )

        if (unknownReasonValidation != null) {
            return unknownReasonValidation.message
        }

        val validation = field.findInvalidValidation(RequiredValidation::class.java)

        if (validation != null) {
            return ResourcesUtils.getString(R.string.error_required_value)
        }

        return ResourcesUtils.getString(R.string.error_invalid_value)
    }
}
