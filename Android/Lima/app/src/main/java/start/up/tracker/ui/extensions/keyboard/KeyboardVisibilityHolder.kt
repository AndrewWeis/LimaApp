package start.up.tracker.ui.extensions.keyboard

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

private const val MIN_KEYBOARD_HEIGHT_PX = 150

/**
 * Держатель информации видимости клавиатуры
 */
class KeyboardVisibilityHolder {

    /**
     * Задать слушатель на видимость клавиатуры
     *
     * @param activity       @[Activity]
     * @param lifecycleOwner @[LifecycleOwner]
     * @param listener       слушатель видимости клавиатуры
     */
    fun setListener(
        activity: Activity, lifecycleOwner: LifecycleOwner, listener: KeyboardVisibilityListener
    ) {
        val globalLayoutListener = getGlobalLayoutListener(activity, listener)

        addGlobalLayoutListener(activity, globalLayoutListener)

        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeGlobalLayoutListener(activity, globalLayoutListener)
            }
        })
    }

    /**
     * Добавить слушатель @[OnGlobalLayoutListener]
     *
     * @param activity             @[Activity]
     * @param globalLayoutListener @[OnGlobalLayoutListener]
     */
    private fun addGlobalLayoutListener(
        activity: Activity, globalLayoutListener: OnGlobalLayoutListener
    ) {
        getActivityRootView(activity)
            ?.viewTreeObserver
            ?.addOnGlobalLayoutListener(globalLayoutListener)
    }

    /**
     * Удалить слушатель @[OnGlobalLayoutListener]
     *
     * @param activity             @[Activity]
     * @param globalLayoutListener @[OnGlobalLayoutListener]
     */
    private fun removeGlobalLayoutListener(
        activity: Activity, globalLayoutListener: OnGlobalLayoutListener
    ) {
        getActivityRootView(activity)
            ?.viewTreeObserver
            ?.removeOnGlobalLayoutListener(globalLayoutListener)
    }

    /**
     * Получить @[OnGlobalLayoutListener]
     *
     * @param activity @[Activity]
     * @param listener @[KeyboardVisibilityListener]
     * @return @[OnGlobalLayoutListener]
     */
    private fun getGlobalLayoutListener(
        activity: Activity, listener: KeyboardVisibilityListener
    ): OnGlobalLayoutListener {

        return object : OnGlobalLayoutListener {

            private var wasOpened = false

            override fun onGlobalLayout() {
                val isOpen = isKeyboardVisible(activity)

                if (isOpen == wasOpened) {
                    return
                }

                if (isOpen) {
                    listener.onKeyboardShow()
                } else {
                    listener.onKeyboardHide()
                }

                wasOpened = isOpen
            }
        }
    }

    /**
     * Получить статус видимости клавитуры
     *
     * @param activity @[Activity]
     * @return true - клавиатура показан / иначе не показана
     */
    private fun isKeyboardVisible(activity: Activity): Boolean {

        val activityRoot = getActivityRootView(activity) ?: return false
        val rootViewGroup = getRootViewGroup(activity) ?: return false

        val rect = Rect()
        activityRoot.getWindowVisibleDisplayFrame(rect)

        val location = IntArray(2)
        rootViewGroup.getLocationOnScreen(location)

        val screenHeight = activityRoot.rootView.height
        val heightDiff = screenHeight - rect.height() - location[1]

        return heightDiff > MIN_KEYBOARD_HEIGHT_PX
    }

    /**
     * Получить главную @[View] @[Activity]
     *
     * @param activity @[Activity]
     * @return главная @[View]
     */
    private fun getActivityRootView(activity: Activity): View? =
        getRootViewGroup(activity)?.getChildAt(0)

    private fun getRootViewGroup(activity: Activity): ViewGroup? =
        activity.findViewById(android.R.id.content)
}
