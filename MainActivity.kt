package com.example.todolistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var etNewTask: EditText
    private lateinit var btnAdd: Button
    private lateinit var rvTasks: RecyclerView

    private val taskList = mutableListOf<ToDoItem>()
    private lateinit var adapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNewTask = findViewById(R.id.etNewTask)
        btnAdd    = findViewById(R.id.btnAdd)
        rvTasks   = findViewById(R.id.rvTasks)


        loadTasks()


        adapter = ToDoAdapter(taskList) { saveTasks() }
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                taskList.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                saveTasks()
            }
        }).attachToRecyclerView(rvTasks)


        btnAdd.setOnClickListener {
            val text = etNewTask.text.toString().trim()
            if (text.isNotEmpty()) {
                adapter.addTask(ToDoItem(text))
                etNewTask.text.clear()
                rvTasks.scrollToPosition(taskList.size - 1)
            }
        }
    }


    private fun saveTasks() {
        val prefs = getSharedPreferences("ToDoPrefs", MODE_PRIVATE)
        val json  = Gson().toJson(taskList)
        prefs.edit().putString("tasks", json).apply()
    }


    private fun loadTasks() {
        val prefs = getSharedPreferences("ToDoPrefs", MODE_PRIVATE)
        prefs.getString("tasks", null)?.let { json ->
            val type = object : TypeToken<MutableList<ToDoItem>>() {}.type
            val list: MutableList<ToDoItem> = Gson().fromJson(json, type)
            taskList.clear()
            taskList.addAll(list)
        }
    }
}
