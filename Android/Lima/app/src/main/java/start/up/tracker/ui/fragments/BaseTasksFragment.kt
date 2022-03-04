package start.up.tracker.ui.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

open class BaseTasksFragment(
    val layoutResId: Int
) : Fragment(layoutResId) {

    fun showUndoDeleteSnackbar(action: () -> Unit) {
        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
            .setAction("UNDO") { action }
            .show()
    }

    fun showTaskSavedMessage(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

    fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }
}
