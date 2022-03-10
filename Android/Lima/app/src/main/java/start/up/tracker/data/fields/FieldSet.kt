package start.up.tracker.data.fields

/**
 * Набор полей с дополнительной информацией о них
 */
abstract class FieldSet {

    private val fields: MutableMap<Enum<*>, Field<*>> = HashMap()

    fun validate() {
        fields.values.forEach(Field<*>::validate)
    }

    fun resetValidation() {
        fields.values.forEach(Field<*>::resetValidation)
    }

    fun isValid(): Boolean {
        return fields.values.fold(true) { isValid, field ->
            isValid && field.isValid()
        }
    }

    protected fun addField(fieldName: Enum<*>, field: Field<*>) {
        fields[fieldName] = field
    }

    protected fun getField(fieldName: Enum<*>): Field<*> {
        return fields[fieldName]!!
    }
}
