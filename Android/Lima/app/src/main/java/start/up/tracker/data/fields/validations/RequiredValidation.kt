package start.up.tracker.data.fields.validations

/**
 * Валидатор на обязательность ввода
 */
class RequiredValidation : Validation {

    override fun <T> isValid(value: T): Boolean {
        return !isEmptyValue(value)
    }

    private fun <T> isEmptyValue(value: T?): Boolean {

        if (value == null) {
            return true
        }

        if (value is String) {
            return value.isEmpty()
        }

        return false
    }
}
