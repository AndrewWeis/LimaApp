package start.up.tracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import start.up.tracker.data.db.Task
import start.up.tracker.databinding.ItemTaskBinding

class TaskAdapter (private val listener: (Task) -> Unit) : ListAdapter<Task, TaskAdapter.NoteViewHolder>(DiffUtilNote()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item, listener)
    }

    class NoteViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(task: Task, listener: (Task) -> Unit) {
            binding.apply {
                label.text = task.label
            }
            binding.root.setOnClickListener {
                listener(task)
            }
        }
    }

    fun getNoteAt(position: Int): Task {
        return getItem(position)
    }

    private class DiffUtilNote : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return newItem == oldItem
        }
    }
}