package com.example.mobile

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    enum class FilterType {
        ALL, COMPLETED, PENDING
    }

    private lateinit var adapter: TaskAdapter
    private val allTasks = mutableListOf<Task>()
    private val filteredTasks = mutableListOf<Task>()
    private var currentFilter = FilterType.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextTask = findViewById<EditText>(R.id.editTextTask)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val buttonAll = findViewById<Button>(R.id.buttonAll)
        val buttonCompleted = findViewById<Button>(R.id.buttonCompleted)
        val buttonPending = findViewById<Button>(R.id.buttonPending)

        adapter = TaskAdapter(
            tasks = filteredTasks,
            onToggleComplete = { updatedTask ->
                val index = allTasks.indexOfFirst { it.id == updatedTask.id }
                if (index != -1) {
                    allTasks[index].completed = updatedTask.completed
                    applyFilter()
                }
            },
            onDelete = { deletedTask ->
                allTasks.removeAll { it.id == deletedTask.id }
                applyFilter()
            }
        )

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = adapter

        buttonAdd.setOnClickListener {
            val title = editTextTask.text.toString().trim()
            if (title.isNotEmpty()) {
                val task = Task(id = allTasks.size + 1, title = title)
                allTasks.add(task)
                editTextTask.text.clear()
                applyFilter()
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        buttonAll.setOnClickListener {
            currentFilter = FilterType.ALL
            applyFilter()
        }

        buttonCompleted.setOnClickListener {
            currentFilter = FilterType.COMPLETED
            applyFilter()
        }

        buttonPending.setOnClickListener {
            currentFilter = FilterType.PENDING
            applyFilter()
        }

        applyFilter() // Initial load
    }

    private fun applyFilter() {
        filteredTasks.clear()
        filteredTasks.addAll(
            when (currentFilter) {
                FilterType.ALL -> allTasks
                FilterType.COMPLETED -> allTasks.filter { it.completed }
                FilterType.PENDING -> allTasks.filter { !it.completed }
            }
        )
        adapter.notifyDataSetChanged()
    }
}
