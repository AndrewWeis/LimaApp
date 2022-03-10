package start.up.tracker.ui.extensions

import start.up.tracker.data.fields.Field
import start.up.tracker.data.fields.validations.RequiredValidation
import start.up.tracker.data.fields.validations.UnknownReasonValidation

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
            return "значение"
        }

        return "значение"
    }
}
