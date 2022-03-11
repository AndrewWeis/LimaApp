package start.up.tracker.ui.views.forms.base

import android.text.Editable
import android.view.View
import androidx.annotation.DrawableRes
import start.up.tracker.databinding.TextInputItemBinding
import start.up.tracker.ui.data.entities.forms.Error
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.Settings

open class BaseInputView(
    private val binding: TextInputItemBinding
) {

    open fun bind(listItem: ListItem, settings: Settings?, listener: TextInputListener) {
        showText(listItem.data as String)

        settings?.let {
            showHint(settings.hint)
            showName(settings.name)
            showInputLayoutError(listItem.error)
            setEndIcon(settings.icon)
            setEditable(settings.editable)
            setInputType(settings.inputType)
            setImeOptions(settings.imeOption)
            setMaxLines(settings.maxLines)
            setListeners(listItem, listener)
        }
    }

    fun disableInputMode() {
        binding.textInputEditText.apply {
            isFocusable = false
            isCursorVisible = false
            keyListener = null
        }
    }

    fun showInputLayoutError(error: Error) {
        error.message?.let {
            binding.textInputLayout.error = it
        }
    }

    protected fun setEndIconBtnClick(onClickListener: View.OnClickListener?) {
        binding.endIconButton.setOnClickListener(onClickListener)
    }

    protected fun clearEditText() {
        binding.textInputEditText.editableText.clear()
    }

    protected fun setEndIcon(@DrawableRes res: Int?) {
        if (res == null) {
            setEndIconVisibility(false)
            return
        }

        setEndIconVisibility(true)
        binding.endIconButton.setImageResource(res)
    }

    protected fun onFocusGain(view: View?) {}

    protected fun onFocusLose(view: View?) {}

    private fun showText(value: String?) {
        val editable: Editable = binding.textInputEditText.text ?: return
        editable.clear()
        value?.let { editable.append(value) }
    }

    private fun showHint(hint: String?) {
        hint?.let {
            binding.textInputEditText.hint = it
        }
    }

    private fun showName(name: String?) {
        name?.let {
            binding.fieldNameText.text = it
        }
    }

    private fun setEditable(isEditable: Boolean) {
        binding.textInputEditText.apply {
            isEnabled = isEditable
            isFocusable = isEditable
        }
        binding.endIconButton.alpha = if (isEditable) 1f else 0.3f
    }

    private fun setInputType(inputType: Int?) {
        inputType?.let {
            binding.textInputEditText.inputType = it
        }
    }

    private fun setImeOptions(imeOption: Int?) {
        imeOption?.let {
            binding.textInputEditText.imeOptions = it
        }
    }

    private fun setMaxLines(maxLines: Int?) {
        maxLines?.let {
            binding.textInputEditText.maxLines = it
            binding.textInputEditText.isSingleLine = it == 1
        }
    }

    private fun setListeners(listItem: ListItem, listener: TextInputListener?) {
        if (listener == null) {
            return
        }

        binding.textInputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                listener.onFocusLose(listItem)
                onFocusLose(view)
            } else {
                listener.onFocusGain(listItem)
                onFocusGain(view)
            }
        }
    }

    private fun setEndIconVisibility(visibility: Boolean) {
        binding.endIconButton.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    interface TextInputListener {
        fun onTextInputDataChange(listItem: ListItem)
        fun onFocusLose(listItem: ListItem)
        fun onFocusGain(listItem: ListItem)
        fun onClearClick(listItem: ListItem)
        fun onDoneClick(item: ListItem)
    }
}
