package start.up.tracker.ui.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.entities.Category
import start.up.tracker.databinding.ItemCategoryBinding

class CategoriesAdapter(private val listener: OnItemClickListener) : ListAdapter<Category,
        CategoriesAdapter.CategoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val category = getItem(position)
                        listener.onItemClick(category)
                    }
                }
            }
        }

        fun bind(category: Category) {
            binding.apply {
                textViewCategoryName.text = category.categoryName
                icCategory.background.setTint(category.color)
                textViewTasksNumber.text = category.tasksInside.toString()
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.categoryId == newItem.categoryId

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }
}
