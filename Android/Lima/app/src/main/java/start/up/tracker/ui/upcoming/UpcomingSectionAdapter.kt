package start.up.tracker.ui.upcoming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.models.ExtendedTask
import start.up.tracker.databinding.ItemTaskExtendedBinding
import start.up.tracker.utils.chooseIconDrawable


class UpcomingSectionAdapter : ListAdapter<ExtendedTask,
        UpcomingSectionAdapter.SectionsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsViewHolder {
        val binding = ItemTaskExtendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SectionsViewHolder(private val binding: ItemTaskExtendedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(extendedTask: ExtendedTask) {
            binding.apply {
                checkBoxCompleted.isChecked = extendedTask.completed
                textViewName.text = extendedTask.taskName
                textViewName.paint.isStrikeThruText = extendedTask.completed
                textCategoryName.text = extendedTask.categoryName
                textCategoryName.setTextColor(extendedTask.color)
                categoryCircle.background.setTint(extendedTask.color)

                if (extendedTask.priority == 4) {
                    icPriority.visibility = View.GONE
                } else {
                    icPriority.setImageResource(chooseIconDrawable(extendedTask.priority))
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<ExtendedTask>() {
        override fun areItemsTheSame(oldItem: ExtendedTask, newItem: ExtendedTask) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ExtendedTask, newItem: ExtendedTask) =
            oldItem == newItem
    }
}