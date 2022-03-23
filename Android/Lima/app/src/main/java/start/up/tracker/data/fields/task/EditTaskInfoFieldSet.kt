package start.up.tracker.data.fields.task

import start.up.tracker.data.fields.Field
import start.up.tracker.data.fields.FieldSet
import start.up.tracker.data.fields.validations.RequiredValidation
import start.up.tracker.entities.Task

class EditTaskInfoFieldSet(
    task: Task
) : FieldSet() {

    init {
        addTaskTitleField(task.taskTitle)
    }

    fun getTitleField(): Field<String> {
        return getField(EditTaskInfo.TITLE) as Field<String>
    }

    fun onTitleChange(title: String) {
        getTitleField().setValue(title)
    }

    private fun addTaskTitleField(title: String) {
        val field = Field(title)
        field.addValidation(RequiredValidation())
        addField(EditTaskInfo.TITLE, field)
    }

    private enum class EditTaskInfo {
        TITLE
    }
}
