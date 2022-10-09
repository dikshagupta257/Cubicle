package com.hackathon.cubicle.employee.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.cubicle.R
import com.hackathon.cubicle.employee.data.model.TaskModel
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(private val list : List<TaskModel>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        @SuppressLint("SimpleDateFormat")
        fun bind(item:TaskModel) = with(itemView){
            val colors = context.resources.getIntArray(R.array.random_color)
            val randomColor = colors[Random().nextInt(colors.size)]

            val viewColorTag = findViewById<View>(R.id.viewColorTag)
            val txtShowTitle = findViewById<TextView>(R.id.txtShowTitle)
            val txtShowTask = findViewById<TextView>(R.id.txtShowTask)
            val txtShowCategory = findViewById<TextView>(R.id.txtShowCategory)
            val txtShowDateTime = findViewById<TextView>(R.id.txtShowDateTime)
            val txtShowTimeDur = findViewById<TextView>(R.id.txtShowTimeDur)
            //setting up the views of item_todo layout
            viewColorTag.setBackgroundColor(randomColor)
            txtShowTitle.text = item.title
            txtShowTask.text = item.description
            txtShowCategory.text = item.category

            val myFormat1 = "EEE, d MMM yyyy"
            val sdf1 = SimpleDateFormat(myFormat1)

            val myFormat2 = "h:mm a"
            val sdf2 = SimpleDateFormat(myFormat2)

            txtShowDateTime.text = sdf1.format(Date(item.date)) + ", " + sdf2.format(Date(item.time))


            txtShowTimeDur.text = item.timeTaken.toString() + "mins"


        }

    }

}