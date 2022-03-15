package start.up.tracker.ui.fragments.tasks.base

import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import start.up.tracker.R
import start.up.tracker.ui.fragments.base.BaseFragment
import start.up.tracker.utils.resources.ResourcesUtils

open class BaseTasksFragment(
    layoutResId: Int
) : BaseFragment(layoutResId) {

    fun showUndoDeleteSnackbar(action: () -> Unit) {
        Snackbar.make(
            requireView(),
            ResourcesUtils.getString(R.string.task_deleted),
            Snackbar.LENGTH_LONG
        )
            .setAction(ResourcesUtils.getString(R.string.undo)) {
                action()
            }
            .show()
    }

    fun showTaskSavedMessage(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

    fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }
}
