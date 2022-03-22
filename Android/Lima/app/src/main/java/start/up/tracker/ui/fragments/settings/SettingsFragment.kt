package start.up.tracker.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.database.SettingsStorage
import start.up.tracker.databinding.SettingsFragmentBinding
import start.up.tracker.mvvm.view_models.settings.SettingsViewModel
import start.up.tracker.mvvm.view_models.settings.SettingsViewModel.SettingEvent
import start.up.tracker.ui.data.entities.settings.Setting
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseNavigationFragment
import start.up.tracker.ui.list.adapters.settings.SettingsAdapter
import start.up.tracker.ui.list.generators.settings.SettingsGenerator
import start.up.tracker.ui.list.view_holders.settings.SettingsViewHolder

@AndroidEntryPoint
class SettingsFragment :
    BaseNavigationFragment(R.layout.settings_fragment),
    SettingsViewHolder.OnSettingClickListener {

    private val viewModel: SettingsViewModel by viewModels()

    private var binding: SettingsFragmentBinding? = null

    private lateinit var adapter: SettingsAdapter
    private var listExtension: ListExtension? = null
    private val generator = SettingsGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SettingsFragmentBinding.bind(view)

        setupAdapter()
        setupData()
        setupSettingEventsListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onSettingClick(setting: Setting) {
        viewModel.onSettingClick(setting)
    }

    private fun setupSettingEventsListener() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.settingsEvent.collect { event ->
            when (event) {
                is SettingEvent.NavigateToAppearance -> {
                    // todo (implement the opportunity to select an appearance of the app)
                }

                is SettingEvent.NavigateToTechniques -> {
                    val action = SettingsFragmentDirections.actionSettingsToTechniques()
                    navigateTo(action)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = SettingsAdapter(
            layoutInflater = layoutInflater,
            listener = this
        )

        listExtension = ListExtension(binding?.settingsList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun setupData() {
        val settings = SettingsStorage().getSettings()
        adapter.updateItems(generator.createListItems(settings))
    }
}
