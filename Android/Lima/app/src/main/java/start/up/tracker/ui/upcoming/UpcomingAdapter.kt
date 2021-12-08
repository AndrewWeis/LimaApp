package start.up.tracker.ui.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.models.UpcomingSection
import start.up.tracker.databinding.ItemUpcomingSectionBinding


class UpcomingAdapter : ListAdapter<UpcomingSection,
        UpcomingAdapter.SectionsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsViewHolder {
        val binding = ItemUpcomingSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SectionsViewHolder(private val binding: ItemUpcomingSectionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(upcomingSection: UpcomingSection) {
            binding.apply {
               sectionTitle.text = upcomingSection.section

            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<UpcomingSection>() {
        override fun areItemsTheSame(oldItem: UpcomingSection, newItem: UpcomingSection) =
            oldItem.section == newItem.section

        override fun areContentsTheSame(oldItem: UpcomingSection, newItem: UpcomingSection) =
            oldItem == newItem
    }
}