package start.up.tracker.ui.extensions.keyboard

/**
 * Слушатель на видимость клавиатуры
 */
interface KeyboardVisibilityListener {

    /**
     * Срабатывает при показе клавиатуры
     */
    fun onKeyboardShow() {
    }

    /**
     * Срабатывает при скрытии клавиатуры
     */
    fun onKeyboardHide() {
    }
}
