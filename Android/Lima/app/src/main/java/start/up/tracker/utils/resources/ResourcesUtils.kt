package start.up.tracker.utils.resources

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.Window
import androidx.annotation.*
import androidx.core.content.ContextCompat
import start.up.tracker.application.App
import java.util.*

/**
 * Помощник для работы с файлами ресурсов
 */
object ResourcesUtils {

    private val DEFAULT_APP_LOCALE: Locale = Locale("ru")

    /**
     * Получение строки из ресурса
     *
     * @param stringRes идентификатор строкового ресурса
     * @return строка
     */
    fun getString(stringRes: Int) = getResources().getString(stringRes)

    /**
     * Получение строки из ресурса с параметрами
     *
     * @param stringRes идентификатор строкового ресурса
     * @param args      параметры для строки
     * @return строка
     */
    fun getString(@StringRes stringRes: Int, vararg args: Any?) = getResources().getString(stringRes, *args)

    /**
     * Возвращает цвет
     *
     * @param resColor идентификатор ресурса цвета
     * @return цвет
     */
    fun getColor(@ColorRes resColor: Int) = ContextCompat.getColor(App.context, resColor)

    /**
     * Возвращает список значених полученых из файла ресурса
     *
     * @param resId resource array
     * @return значения в виде TypedArray
     */
    fun getValuesFromResArray(resId: Int): IntArray {
        return getResources().getIntArray(resId)
    }

    /**
     * Возвращает drawable
     *
     * @param resDrawable идентификатор ресурса drawable
     * @return drawable
     */
    fun getDrawable(@DrawableRes resDrawable: Int) =
        ContextCompat.getDrawable(App.context, resDrawable)

    /**
     * Возвращает строку с окончанием множества
     *
     * @param pluralId идентификатор текста множества
     * @param quantity количество
     * @return строка
     */
    fun getQuantityString(@PluralsRes pluralId: Int, quantity: Long) =
        getQuantityString(pluralId, quantity, quantity)

    /**
     * Возвращает строку с окончанием множества
     *
     * @param pluralId идентификатор текста множества
     * @param quantity количество
     * @param value    подставляемое значение
     * @return строка
     */
    fun getQuantityString(@PluralsRes pluralId: Int, quantity: Long, value: Any): String {
        val localizedResources = getLocalizedResources(DEFAULT_APP_LOCALE)
        val integerQuantity = if (quantity > Int.MAX_VALUE) (quantity % 100).toInt()
        else quantity.toInt()
        return localizedResources.getQuantityString(pluralId, integerQuantity, value)
    }

    /**
     * Возвращает количество pixels из ресурсов через ссылку на dimension resource
     *
     * @param dimenId индентификатор dimension resource
     * @return количество pixels
     * @see .getPxByDp
     */
    fun getDimensionPixels(@DimenRes dimenId: Int) = getResources().getDimensionPixelSize(dimenId)

    /**
     * Возвращает ширину экрана в pixels
     *
     * @return количество pixels
     */
    fun getScreenWidthInPixels() = getDisplayMetrics().widthPixels

    /**
     * Возвращает высоту экрана в pixels
     *
     * @return количество pixels
     */
    fun getScreenHeightInPixels(): Int {
        val displayHeight = getDisplayMetrics().heightPixels
        val statusBarHeight = getStatusBarHeight()
        return displayHeight - statusBarHeight
    }

    /**
     * Возвращает высоту экрана в pixels
     *
     * @return количество pixels
     */
    fun getScreenHeightInPixels(window: Window): Int {
        val decorView = window.decorView
        val rect = Rect()
        decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = getStatusBarHeight()
        return if (rect.top == 0) rect.bottom else rect.bottom - statusBarHeight
    }

    /**
     * Конвертирует значение dp в значение pixels
     *
     * @param dp количество dp
     * @return количество pixels
     * @see .getDimensionPixels
     */
    fun getPxByDp(dp: Float): Int {
        val metrics = getDisplayMetrics()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }

    /**
     * Получение целого числа по его идентификатору
     *
     * @param integerResId идентификатор ресурса целого числа
     * @return целое число
     */
    fun getInteger(@IntegerRes integerResId: Int) = getResources().getInteger(integerResId)

    /**
     * Возвращает массив строк
     *
     * @param arrayResId индентификатор ресурса массива
     * @return массив строк
     */
    fun getStringArray(@ArrayRes arrayResId: Int) = getResources().getStringArray(arrayResId)

    /**
     * Возвращает высоту StatusBar в pixels
     *
     * @return высота StatusBar в pixels
     */
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = getResources().getIdentifier(
            "status_bar_height",
            "dimen", "android"
        )
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Возвращает экземпляр класса Resources
     *
     * @return экземпляр класса Resources
     */
    private fun getResources() = App.context.resources

    private fun getDisplayMetrics() = getResources().displayMetrics

    /**
     * Возвращает локализированный экземпляр класса [Resources]
     *
     * @param locale [языковая локаль][Locale]
     * @return [Resources] с применённой локалью
     */
    private fun getLocalizedResources(locale: Locale): Resources {
        var conf = getResources().configuration
        conf = Configuration(conf)
        conf.setLocale(locale)
        return App.context.createConfigurationContext(conf).resources
    }
}
