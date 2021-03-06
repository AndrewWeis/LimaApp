package start.up.tracker.ui.fragments.techniques

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.TechniquesFragmentBinding
import start.up.tracker.entities.Technique
import start.up.tracker.mvvm.view_models.techniques.TechniquesViewModel
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.list.adapters.techniques.TechniquesAdapter
import start.up.tracker.ui.list.generators.techniques.TechniqueGenerator
import start.up.tracker.ui.list.view_holders.techniques.TechniqueViewHolder

@AndroidEntryPoint
class TechniquesFragment :
    Fragment(R.layout.techniques_fragment),
    TechniqueViewHolder.OnTechniqueClickListener {

    private val viewModel: TechniquesViewModel by viewModels()

    private var binding: TechniquesFragmentBinding? = null

    private lateinit var adapter: TechniquesAdapter
    private var listExtension: ListExtension? = null
    private val generator = TechniqueGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TechniquesFragmentBinding.bind(view)

        initAdapter()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onTechniqueClick(technique: Technique) {
        val action = TechniquesFragmentDirections.actionTechniquesToTechnique(technique, technique.title)
        findNavController().navigate(action)
    }

    private fun showTechniques(techniques: List<Technique>) {
        adapter.updateItems(generator.createListItems(techniques))
    }

    private fun initObservers() {
        viewModel.techniques.observe(viewLifecycleOwner) {
            showTechniques(it)
        }
    }

    private fun initAdapter() {
        adapter = TechniquesAdapter(
            layoutInflater = layoutInflater,
            listener = this
        )

        listExtension = ListExtension(binding?.list)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }
}