package start.up.tracker.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.db.models.Category
import start.up.tracker.databinding.ItemCategoryBinding

class CategoriesAdapter : ListAdapter<Category,
        CategoriesAdapter.CategoryViewHolder>(DiffCallback()
) {

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

    class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                textViewCategoryName.text = category.categoryName
                // TODO(Implement the number of tasks inside the category)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }
}
