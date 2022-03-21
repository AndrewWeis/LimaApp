package start.up.tracker.ui.list.view_holders.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import start.up.tracker.R
import start.up.tracker.databinding.SettingItemBinding
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.settings.Setting
import start.up.tracker.ui.list.view_holders.base.BaseViewHolder

class SettingsViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder(layoutInflater, parent, R.layout.setting_item) {

    private var binding = SettingItemBinding.bind(itemView)

    private lateinit var setting: Setting
    private lateinit var listener: OnSettingClickListener

    fun bind(listItem: ListItem, listener: OnSettingClickListener) {
        this.setting = listItem.data as Setting
        this.listener = listener

        setupSettingTitle()
        setupSettingImage()
        setupClickListener()
    }

    private fun setupSettingImage() {
        binding.settingImage.setImageResource(setting.imageRes)
    }

    private fun setupSettingTitle() {
        binding.settingTitle.text = setting.title
    }

    private fun setupClickListener() {
        binding.root.setOnClickListener {
            listener.onSettingClick(setting)
        }
    }

    interface OnSettingClickListener {
        fun onSettingClick(setting: Setting)
    }
}
