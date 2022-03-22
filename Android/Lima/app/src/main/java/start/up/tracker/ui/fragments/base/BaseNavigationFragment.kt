package start.up.tracker.ui.fragments.base

import androidx.annotation.LayoutRes
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BaseNavigationFragment(
    @LayoutRes contentLayoutId: Int
) : BaseFragment(contentLayoutId) {

    fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }
}
