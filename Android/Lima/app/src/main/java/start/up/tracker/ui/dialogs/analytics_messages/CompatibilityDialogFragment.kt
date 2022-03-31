package start.up.tracker.ui.dialogs.analytics_messages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.databinding.CompatibilityDialogFragmentBinding

@AndroidEntryPoint
class CompatibilityDialogFragment :
    DialogFragment(R.layout.compatibility_dialog_fragment) {

    private var binding: CompatibilityDialogFragmentBinding? = null

    private val args by navArgs<CompatibilityDialogFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CompatibilityDialogFragmentBinding.bind(view)

        setupListeners()

        showMessagesData(args.message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupListeners() {
        binding?.okButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showMessagesData(message: AnalyticsMessage) {
        binding?.message?.apply {
            messageTitleText.text = message.title
            messageErrorText.text = message.error
            messageHintText.text = message.hint
        }
    }
}
