package start.up.tracker.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.db.Category
import start.up.tracker.databinding.ItemCategoryBinding

class CategoriesAdapter/*(private val listener: OnItemClickListener)*/ : ListAdapter<Category,
        CategoriesAdapter.CategoryViewHolder>(CategoriesAdapter.DiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.CategoryViewHolder, position: Int) {
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
                     //   listener.onItemClick(category)
                    }
                }
            }

            // TODO(Implement onClickListener on Image)
        }

        fun bind(category: Category) {
            binding.apply {
                textViewCategoryName.text = category.categoryName
                // TODO(Implement the number of tasks inside the category)
                // TODO(Implement setting chosen Image)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
        // TODO(Implement onImageClick logic)
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }
}
