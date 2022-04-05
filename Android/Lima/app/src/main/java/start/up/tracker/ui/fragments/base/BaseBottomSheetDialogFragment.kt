package start.up.tracker.ui.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import start.up.tracker.ui.extensions.keyboard.KeyboardEventManager
import start.up.tracker.ui.extensions.screen_locker.ScreenLocker

abstract class BaseBottomSheetDialogFragment(
    @LayoutRes private val layoutRes: Int,
) : BottomSheetDialogFragment(),
    ScreenLocker {

    private val keyboardEventManager = KeyboardEventManager()

    override fun getScreenWindow(): Window? {
        return activity?.window
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
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

    fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }
}
