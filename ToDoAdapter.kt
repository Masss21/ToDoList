package com.example.todolistapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(
    private val items: MutableList<ToDoItem>,
    private val onItemChanged: () -> Unit
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbDone: CheckBox = view.findViewById(R.id.cbDone)
        val tvTask: TextView = view.findViewById(R.id.tvTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val item = items[position]


        holder.cbDone.setOnCheckedChangeListener(null)


        holder.tvTask.text = item.task
        holder.cbDone.isChecked = item.isDone
        holder.tvTask.paintFlags = if (item.isDone)
            holder.tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            holder.tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()


        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            item.isDone = isChecked
            notifyItemChanged(position)
            onItemChanged()
        }
    }

    override fun getItemCount(): Int = items.size

    fun addTask(item: ToDoItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
        onItemChanged()
    }
}
