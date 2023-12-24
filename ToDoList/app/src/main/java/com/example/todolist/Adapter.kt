package com.example.todolist

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter(private var data: List<CardInfo>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: android.widget.TextView? = itemView.findViewById(R.id.title)
        var priority: android.widget.TextView? = itemView.findViewById(R.id.priority)
        var layout: android.widget.LinearLayout? = itemView.findViewById(R.id.my_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (data[position].priority.lowercase()) {
            "high" -> holder.layout?.setBackgroundColor(Color.parseColor("#F05454"))
            "medium" -> holder.layout?.setBackgroundColor(Color.parseColor("#EDC988"))
            else -> holder.layout?.setBackgroundColor(Color.parseColor("#00917C"))
        }

        holder.title?.text ?: data[position].title
        holder.priority?.text ?: data[position].priority
        holder.itemView.setOnClickListener{
            val intent= Intent(holder.itemView.context,UpdateCard::class.java)
            intent.putExtra("id",position)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
