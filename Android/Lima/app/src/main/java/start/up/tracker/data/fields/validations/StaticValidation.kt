package start.up.tracker.data.fields.validations

/**
 * Статичное правило валидации
 */
class StaticValidation(private val isValid: Boolean) : Validation {
    override fun <T> isValid(value: T): Boolean {
        return isValid
    }
}
