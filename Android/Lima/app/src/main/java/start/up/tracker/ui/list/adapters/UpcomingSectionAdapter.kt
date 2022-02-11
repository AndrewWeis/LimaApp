package start.up.tracker.ui.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.entities.ExtendedTask
import start.up.tracker.databinding.ItemTaskExtendedBinding
import start.up.tracker.utils.chooseIconDrawable


class UpcomingSectionAdapter(private val listener: OnItemClickListener) : ListAdapter<ExtendedTask,
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

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                checkBoxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkBoxCompleted.isChecked)
                    }
                }
            }
        }

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
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: ExtendedTask, newItem: ExtendedTask) =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(extendedTask: ExtendedTask)
        fun onCheckBoxClick(extendedTask: ExtendedTask, isChecked: Boolean)
    }
}