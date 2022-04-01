package start.up.tracker.ui.extensions.screen_locker

import android.view.Window
import android.view.WindowManager

/**
 * Блокировщик экрана
 */
interface ScreenLocker {

    /**
     * Возвращает [Window] экрана
     *
     * @return [Window]
     */
    fun getScreenWindow(): Window?

    /**
     * Блокировать экран
     */
    fun lockScreen() {
        getScreenWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * Разблокировать экран
     */
    fun unlockScreen() {
        getScreenWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
