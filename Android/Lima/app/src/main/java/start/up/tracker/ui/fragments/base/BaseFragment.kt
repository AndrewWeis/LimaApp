package start.up.tracker.ui.fragments.base

import android.view.Window
import androidx.fragment.app.Fragment
import start.up.tracker.ui.extensions.keyboard.KeyboardEventManager
import start.up.tracker.ui.extensions.keyboard.KeyboardVisibilityListener
import start.up.tracker.ui.extensions.screen_locker.ScreenLocker

/**
 * Базовый Fragment
 */
abstract class BaseFragment(
    @androidx.annotation.LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId), ScreenLocker {

    private val keyboardEventManager = KeyboardEventManager()

    override fun getScreenWindow(): Window? {
        return activity?.window
    }

    protected open fun onBackPressed() {
        onBackPressed(true)
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

    /**
     * Добавляет слушатель видимости клавиатуры
     *
     * @param listener [слушатель видимости клавиатуры][KeyboardVisibilityListener]
     */
    protected fun addKeyboardVisibilityListener(listener: KeyboardVisibilityListener) {
        keyboardEventManager.setKeyboardVisibilityListener(
            activity,
            viewLifecycleOwner,
            listener
        )
    }
}
