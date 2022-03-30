package start.up.tracker.ui.dialogs.analytics_messages

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessages
import start.up.tracker.databinding.AnalyticsMessagesDialogFragmentBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.ListItemTypes
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.analytics_messages.AnalyticsMessagesDialogAdapter
import start.up.tracker.utils.screens.ExtraCodes

@AndroidEntryPoint
class AnalyticsMessagesDialogFragment :
    DialogFragment(R.layout.analytics_messages_dialog_fragment) {

    private var binding: AnalyticsMessagesDialogFragmentBinding? = null

    private val args by navArgs<AnalyticsMessagesDialogFragmentArgs>()

    private lateinit var adapter: AnalyticsMessagesDialogAdapter
    private var listExtension: ListExtension? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AnalyticsMessagesDialogFragmentBinding.bind(view)

        setupAdapter()
        setupListeners()

        showMessagesData(args.messages)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupListeners() {
        binding?.editButton?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.ignoreButton?.setOnClickListener {
            setFragmentResult(
                requestKey = ExtraCodes.IGNORE_CLICKED_REQUEST,
                result = bundleOf(ExtraCodes.IGNORE_CLICKED_RESULT to 0)
            )
            findNavController().popBackStack()
        }
    }

    private fun setupAdapter() {
        adapter = AnalyticsMessagesDialogAdapter(layoutInflater = layoutInflater)

        listExtension = ListExtension(binding?.analyticsMessagesList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun showMessagesData(analyticsMessages: AnalyticsMessages) {
        val messages = analyticsMessages.messages.map {
            ListItem(type = ListItemTypes.TEXT, data = it)
        }

        adapter.updateItems(messages)
    }
}
