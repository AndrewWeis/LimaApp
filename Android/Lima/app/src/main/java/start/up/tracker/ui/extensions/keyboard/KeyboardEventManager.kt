package start.up.tracker.ui.extensions.keyboard

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import start.up.tracker.application.App

/**
 * Класс управления клавиатурой
 */
class KeyboardEventManager {

    /**
     * Скрыть клавиатуру
     *
     * @param activity @[Activity]
     */
    fun hideKeyboard(activity: Activity?) {
        hideKeyboard(activity?.currentFocus)
    }

    /**
     * Скрыть клавиатуру
     *
     * @param currentFocusedView текущая @[View] с фокусом
     */
    fun hideKeyboard(currentFocusedView: View?) {
        val imm = getInputMethodManager()
        if (currentFocusedView == null || imm == null) {
            return
        }
        imm.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
    }

    /**
     * Показать клавиатуру
     *
     * @param activity @[Activity]
     */
    fun showKeyboard(activity: Activity?) {
        showKeyboard(activity?.currentFocus)
    }

    /**
     * Показать клавиатуру
     *
     * @param view @[View] к которой припрекляется показ клавиатуры
     */
    fun showKeyboard(view: View?) {
        val imm = getInputMethodManager()
        if (view == null || imm == null) {
            return
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Задать слушатель видимости клавиатуры
     *
     * @param activity       @[Activity]
     * @param lifecycleOwner @[LifecycleOwner]
     * @param listener       слушатель видимости клавиатуры
     */
    fun setKeyboardVisibilityListener(
        activity: Activity?, lifecycleOwner: LifecycleOwner, listener: KeyboardVisibilityListener
    ) {
        if (activity == null) {
            return
        }
        KeyboardVisibilityHolder().setListener(activity, lifecycleOwner, listener)
    }

    private fun getInputMethodManager(): InputMethodManager? {
        return App.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }
}
