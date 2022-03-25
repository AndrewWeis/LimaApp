package start.up.tracker.ui.fragments.base

import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import start.up.tracker.ui.extensions.keyboard.KeyboardEventManager
import start.up.tracker.ui.extensions.screen_locker.ScreenLocker

abstract class BaseBottomSheetDialogFragment :
    BottomSheetDialogFragment(),
    ScreenLocker {

    private val keyboardEventManager = KeyboardEventManager()

    override fun getScreenWindow(): Window? {
        return activity?.window
    }

    /**
     * @param shouldHideKeyboard true - клавиатура будет скрыта, иначе остается в текущем состояние
     */
    protected open fun onBackPressed(shouldHideKeyboard: Boolean) {
        if (!shouldHideKeyboard) {
            return
        }

        hideKeyboard()
    }

    /**
     * Скрытие клавиатуры
     */
    protected fun hideKeyboard() {
        keyboardEventManager.hideKeyboard(activity)
    }

    /**
     * Показ клавиатуры
     */
    protected fun showKeyboard() {
        keyboardEventManager.showKeyboard(activity)
    }
}
