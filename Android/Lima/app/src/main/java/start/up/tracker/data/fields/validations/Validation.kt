package start.up.tracker.data.fields.validations

/**
 * Правила валидации значения
 */
interface Validation {
    fun <T> isValid(value: T): Boolean
}
