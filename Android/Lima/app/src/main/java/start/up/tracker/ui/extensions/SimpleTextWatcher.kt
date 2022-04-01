package start.up.tracker.ui.extensions

import android.text.Editable
import android.text.TextWatcher

/**
 * TextWatcher с реализацией по умолчанию
 */
interface SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        onTextChanged(charSequence.toString())
    }

    /**
     * Срабатывает при изменении текста
     *
     * @param textValue значение текста
     */
    fun onTextChanged(textValue: String?)
}
