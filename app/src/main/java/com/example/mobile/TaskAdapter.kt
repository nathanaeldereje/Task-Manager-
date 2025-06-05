package com.example.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onToggleComplete: (Task) -> Unit,
    private val onDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxCompleted = itemView.findViewById<CheckBox>(R.id.checkBoxCompleted)
        val textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
        val buttonDelete = itemView.findViewById<ImageButton>(R.id.buttonDelete)

        fun bind(task: Task) {
            checkBoxCompleted.setOnCheckedChangeListener(null)
            checkBoxCompleted.isChecked = task.completed
            textViewTitle.text = task.title
            textViewTitle.paint.isStrikeThruText = task.completed

            checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
                onToggleComplete(task.copy(completed = isChecked))
            }

            buttonDelete.setOnClickListener {
                onDelete(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }
}
