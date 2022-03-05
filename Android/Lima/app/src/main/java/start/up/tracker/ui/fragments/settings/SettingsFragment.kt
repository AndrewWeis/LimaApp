package start.up.tracker.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import start.up.tracker.R
import start.up.tracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    // todo (recode using adapter)

    private var binding: FragmentSettingsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        initListeners()
        initAppTheme()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // todo(It doesn't work)
    private fun initAppTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            binding?.switchMode?.isChecked = true

        binding?.switchMode?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initListeners() {
        binding?.articlesView?.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsToArticles()
            findNavController().navigate(action)
        }
    }
}
