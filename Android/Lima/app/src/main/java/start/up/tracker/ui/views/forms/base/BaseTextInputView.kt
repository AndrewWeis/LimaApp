package start.up.tracker.ui.views.forms.base

import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import start.up.tracker.R
import start.up.tracker.databinding.TextInputItemBinding
import start.up.tracker.ui.data.entities.forms.ListItem
import start.up.tracker.ui.data.entities.forms.Settings
import start.up.tracker.ui.extensions.SimpleTextWatcher

open class BaseTextInputView(
    private val binding: TextInputItemBinding
) : BaseInputView(binding) {

    private var textWatcher: TextWatcher? = null

    override fun bind(listItem: ListItem, settings: Settings?, listener: TextInputListener) {
        removeTextChangedListener(textWatcher)
        super.bind(listItem, settings, listener)
        setEndIconState(listItem)
        initTextWatcher(listItem, listener)
        setListeners(listItem, listener)
    }

    fun onBindError(listItem: ListItem) {
        setEndIconState(listItem)
        showInputLayoutError(listItem.error)
    }

    fun addTextChangedListener(textWatcher: TextWatcher?) {
        textWatcher?.let {
            binding.textInputEditText.addTextChangedListener(textWatcher)
        }
    }

    fun removeTextChangedListener(textWatcher: TextWatcher?) {
        textWatcher?.let {
            binding.textInputEditText.removeTextChangedListener(textWatcher)
        }
    }

    fun getTextInputEditTextText(): String {
        return binding.textInputEditText.text.toString()
    }

    protected fun onTextChanged(listItem: ListItem, listener: TextInputListener?) {
        setEndIconState(listItem)
        showInputLayoutErrorAfterTextChanged(listItem)
        listener?.onTextInputDataChange(listItem)
    }

    private fun initTextWatcher(listItem: ListItem, listener: TextInputListener) {
        textWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(textValue: String?) {
                listItem.data = textValue
                clearError(listItem)
                this@BaseTextInputView.onTextChanged(listItem, listener)
            }
        }
    }

    /**
     * Показать ошибку после изменения текста
     *
     * @param listItem listItem
     */
    private fun showInputLayoutErrorAfterTextChanged(listItem: ListItem) {
        if (isChangeErrorAfterChangingText(listItem)) {
            showInputLayoutError(listItem.error)
        }
    }

    /**
     * Очистить значение ошибки
     *
     * @param listItem listItem для которого нужно очистить ошибку
     */
    private fun clearError(listItem: ListItem) {
        if (isChangeErrorAfterChangingText(listItem)) {
            listItem.error.message = null
        }
    }

    /**
     * Есть ли возможность изменять данные об ошибке
     *
     * @param listItem listItem
     * @return true - если можно изменять данные, false - если нет
     */
    private fun isChangeErrorAfterChangingText(listItem: ListItem): Boolean {
        val settings: Settings = listItem.settings
        return settings.changeErrorAfterChangingText
    }

    private fun setEndIconState(listItem: ListItem) {
        val settings: Settings = listItem.settings

        val isEditable: Boolean = settings.editable
        val isErrorEmpty = listItem.error.message == null
        val isEmptyText = TextUtils.isEmpty(listItem.data as String)
        var iconRes: Int? = if (isErrorEmpty) R.drawable.ic_clear else R.drawable.ic_error_input

        if (isErrorEmpty && isEmptyText || !isEditable) {
            iconRes = null
        }
        setEndIcon(iconRes)
    }

    private fun setListeners(listItem: ListItem, listener: TextInputListener) {
        addTextChangedListener(textWatcher)
        binding.textInputEditText.setOnEditorActionListener(OnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                listener.onDoneClick(listItem)
                return@OnEditorActionListener true
            }
            false
        })

        setEndIconBtnClick {
            clearEditText()
            listener.onClearClick(listItem)
        }
    }
}
