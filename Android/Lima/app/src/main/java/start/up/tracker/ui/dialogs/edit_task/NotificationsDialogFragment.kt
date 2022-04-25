package start.up.tracker.ui.dialogs.edit_task

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.BaseListDialogFragmentBinding
import start.up.tracker.mvvm.view_models.edit_task.dialogs.NotificationsDialogViewModel
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseBottomSheetDialogFragment
import start.up.tracker.ui.list.adapters.edit_task.dialogs.DialogChoiceAdapter
import start.up.tracker.ui.list.generators.edit_task.dialogs.NotificationsGenerator
import start.up.tracker.ui.list.view_holders.edit_task.dialogs.DialogChoiceViewHolder
import start.up.tracker.utils.screens.ExtraCodes

@AndroidEntryPoint
class NotificationsDialogFragment :
    BaseBottomSheetDialogFragment(R.layout.base_list_dialog_fragment),
    DialogChoiceViewHolder.DialogChoiceClickListener {

    private val viewModel: NotificationsDialogViewModel by viewModels()

    private var binding: BaseListDialogFragmentBinding? = null

    private lateinit var adapter: DialogChoiceAdapter
    private var listExtension: ListExtension? = null
    private val generator = NotificationsGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BaseListDialogFragmentBinding.bind(view)

        setupAdapter()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onChoiceClick(choiceId: Int) {
        viewModel.onNotificationClick(choiceId)

        setFragmentResult(
            requestKey = ExtraCodes.NOTIFICATION_REQUEST,
            result = bundleOf(ExtraCodes.NOTIFICATION_REQUEST to choiceId)
        )

        findNavController().popBackStack()
    }

    private fun showData(notificationId: Int) {
        adapter.updateItems(generator.getDefaultNotificationListItems(notificationId))
    }


    private fun setupAdapter() {
        adapter = DialogChoiceAdapter(
            layoutInflater = layoutInflater,
            dialogChoiceClickListener = this
        )

        listExtension = ListExtension(binding?.dialogList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun setupObservers() {
        viewModel.notificationId.observe(viewLifecycleOwner) { id ->
            showData(id)
        }
    }
}