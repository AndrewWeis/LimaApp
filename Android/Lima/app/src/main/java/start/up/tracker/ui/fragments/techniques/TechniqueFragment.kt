package start.up.tracker.ui.fragments.techniques

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.analytics.entities.AnalyticsMessage
import start.up.tracker.databinding.TechniqueFragmentBinding
import start.up.tracker.entities.Technique
import start.up.tracker.mvvm.view_models.techniques.TechniqueViewModel
import start.up.tracker.ui.fragments.base.BaseNavigationFragment

@AndroidEntryPoint
class TechniqueFragment :
    BaseNavigationFragment(R.layout.technique_fragment) {

    private val viewModel: TechniqueViewModel by viewModels()
    private var binding: TechniqueFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TechniqueFragmentBinding.bind(view)

        setupObservers()
        setupClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        viewModel.technique.observe(viewLifecycleOwner) {
            showButton(it)
            showData(it)
        }

        viewModel.analyticsMessageDialog.observe(viewLifecycleOwner) {
            showDialog(it)
        }
    }

    private fun showDialog(message: AnalyticsMessage) {
        val action = TechniqueFragmentDirections.actionTechniqueToCompatibilityDialog(message)
        navigateTo(action)
    }

    private fun showButton(technique: Technique) {
        if (technique.isEnabled) {
            binding?.techniqueTryOutButton?.visibility = View.GONE
            binding?.techniqueCancelButton?.visibility = View.VISIBLE
        } else {
            binding?.techniqueTryOutButton?.visibility = View.VISIBLE
            binding?.techniqueCancelButton?.visibility = View.GONE
        }
    }

    private fun setupClickListener() {
        binding?.techniqueTryOutButton?.setOnClickListener {
            viewModel.onTechniqueEnableClick()
        }

        binding?.techniqueCancelButton?.setOnClickListener {
            viewModel.onTechniqueDisableClick()
        }
    }

    private fun showData(technique: Technique) {
        binding?.techniqueTitleText?.text = technique.title
        binding?.techniqueBodyText?.text = technique.body
    }
}
