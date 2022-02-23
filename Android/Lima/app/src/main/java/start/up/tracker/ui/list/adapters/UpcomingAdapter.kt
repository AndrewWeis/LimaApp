package start.up.tracker.ui.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import start.up.tracker.data.entities.ExtendedTask
import start.up.tracker.data.entities.UpcomingSection
import start.up.tracker.databinding.ItemUpcomingSectionBinding
import start.up.tracker.mvvm.view_models.UpcomingViewModel


class UpcomingAdapter(val viewModel: UpcomingViewModel) : ListAdapter<UpcomingSection,
        UpcomingAdapter.UpcomingViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        val binding = ItemUpcomingSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class UpcomingViewHolder(private val binding: ItemUpcomingSectionBinding) : RecyclerView.ViewHolder(binding.root),
        UpcomingSectionAdapter.OnItemClickListener {

        fun bind(upcomingSection: UpcomingSection) {
            binding.apply {
                sectionTitle.text = upcomingSection.section

                val sectionAdapter = UpcomingSectionAdapter(this@UpcomingViewHolder)
                sectionRV.apply {
                   adapter = sectionAdapter
                   layoutManager = LinearLayoutManager(context)

                    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            return false
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val task = sectionAdapter.currentList[viewHolder.adapterPosition]
                            viewModel.onTaskSwiped(task)
                        }
                    }).attachToRecyclerView(sectionRV)
                }

                sectionAdapter.submitList(upcomingSection.tasksList)
            }
        }

        override fun onItemClick(extendedTask: ExtendedTask) {
            viewModel.onTaskSelected(extendedTask)
        }

        override fun onCheckBoxClick(extendedTask: ExtendedTask, isChecked: Boolean) {
            viewModel.onTaskCheckedChanged(extendedTask, isChecked)
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<UpcomingSection>() {
        override fun areItemsTheSame(oldItem: UpcomingSection, newItem: UpcomingSection) =
            oldItem.section == newItem.section

        override fun areContentsTheSame(oldItem: UpcomingSection, newItem: UpcomingSection) =
            oldItem == newItem
    }
}