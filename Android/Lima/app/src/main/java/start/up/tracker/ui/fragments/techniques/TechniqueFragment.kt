package start.up.tracker.ui.fragments.techniques

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.TechniqueFragmentBinding
import start.up.tracker.entities.Technique
import start.up.tracker.mvvm.view_models.techniques.TechniqueViewModel

@AndroidEntryPoint
class TechniqueFragment :
    Fragment(R.layout.technique_fragment) {

    private val viewModel: TechniqueViewModel by viewModels()
    private val args by navArgs<TechniqueFragmentArgs>()
    private var binding: TechniqueFragmentBinding? = null

    private lateinit var technique: Technique

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TechniqueFragmentBinding.bind(view)

        this.technique = args.technique

        setupData()
        setupObservers()
        setupClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        viewModel.selectedTechniqueId.observe(viewLifecycleOwner) {
            setupButton(it)
        }
    }

    private fun setupButton(selectedTechniqueId: Int) {
        if (selectedTechniqueId == technique.id) {
            binding?.techniqueTryOutButton?.visibility = View.GONE
            binding?.techniqueCancelButton?.visibility = View.VISIBLE
        } else {
            binding?.techniqueTryOutButton?.visibility = View.VISIBLE
            binding?.techniqueCancelButton?.visibility = View.GONE
        }
    }

    private fun setupClickListener() {
        binding?.techniqueTryOutButton?.setOnClickListener {
            viewModel.onSelectTechniqueClick(technique.id)
        }

        binding?.techniqueCancelButton?.setOnClickListener {
            viewModel.onCancelSelectTechniqueClick()
        }
    }

    private fun setupData() {
        binding?.techniqueTitleText?.text = technique.title
        binding?.techniqueBodyText?.text = technique.body
    }
}