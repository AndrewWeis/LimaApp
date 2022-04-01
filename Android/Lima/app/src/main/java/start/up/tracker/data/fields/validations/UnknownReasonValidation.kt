package start.up.tracker.data.fields.validations

/**
 * Наличие этой проверки говорит о том, что валидация, прозведенная
 * не нами, дала ошибку. Например, ошибка пришла от сервера
 */
class UnknownReasonValidation(val message: String) : Validation {

    override fun <T> isValid(value: T): Boolean {
        return false
    }
}
