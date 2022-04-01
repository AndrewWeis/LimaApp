package start.up.tracker.data.fields

import start.up.tracker.data.fields.validations.RequiredValidation
import start.up.tracker.data.fields.validations.UnknownReasonValidation
import start.up.tracker.data.fields.validations.Validation

/**
 * Поле формы данных
 *
 * @param <T> тип данных значения
 */
class Field<T>(
    private var value: T? = null,
    private var isEditable: Boolean = true,
    private var isValid: Boolean = true
) {

    private val validations: MutableList<Validation> = mutableListOf()

    fun addValidation(validation: Validation) {
        validations.add(validation)
    }

    fun <ValidationClass : Validation> removeValidation(
        validationClass: Class<ValidationClass>
    ) {
        findValidation(validationClass)?.apply {
            validations.remove(this)
        }
    }

    fun getValue(): T? {
        return value
    }

    fun setValue(value: T) {
        resetValidation()
        removeValidation(UnknownReasonValidation::class.java)
        this.value = value
    }

    fun isRequired(): Boolean {
        return findRequiredValidation() != null
    }

    fun isValid(): Boolean {
        return isValid
    }

    fun validate() {
        checkValid()
    }

    fun resetValidation() {
        isValid = true
    }

    fun isEditable(): Boolean {
        return isEditable
    }

    fun setEditable(editable: Boolean) {
        isEditable = editable
    }

    fun <ValidationClass : Validation?> findInvalidValidation(
        validationClass: Class<ValidationClass>
    ): ValidationClass? {
        val validation = findValidation(validationClass)

        if (validation == null || validation.isValid(value)) {
            return null
        }

        return validation
    }

    private fun checkValid() {
        isValid = validations.fold(true) { isValid, field ->
            isValid && field.isValid(value)
        }
    }

    private fun findRequiredValidation(): RequiredValidation? {
        return findValidation(RequiredValidation::class.java)
    }

    private fun <ValidationClass : Validation?> findValidation(
        validationClass: Class<ValidationClass>
    ): ValidationClass? {
        validations.forEach {
            if (validationClass.isInstance(it)) {
                return it as ValidationClass
            }
        }

        return null
    }
}
