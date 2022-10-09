package com.hackathon.cubicle.admin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.cubicle.R
import com.hackathon.cubicle.data.EachEmployeeData
import com.hackathon.cubicle.employee.data.model.TaskModel
import java.util.*

class EachEmployeeAdapter(val items: List<EachEmployeeData>): RecyclerView.Adapter<EachEmployeeAdapter.EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class EmployeeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: EachEmployeeData) = with(itemView){
            val colors = context.resources.getIntArray(R.array.random_color)
            val randomColor = colors[Random().nextInt(colors.size)]

            val viewColorTag = findViewById<View>(R.id.viewColorTag)
            val txtName = findViewById<TextView>(R.id.txtName)
            val txtEmail = findViewById<TextView>(R.id.txtEmail)
            val txtPhone = findViewById<TextView>(R.id.txtPhone)
            val txtShowDept = findViewById<TextView>(R.id.txtShowDept)
            val txtShowDOJ = findViewById<TextView>(R.id.txtShowDOJ)
            val txtShowPass= findViewById<TextView>(R.id.txtShowPass)

            //setting up the values
            viewColorTag.setBackgroundColor(randomColor)
            txtName.text = item.name
            txtEmail.text = item.mail
            txtPhone.text = item.number
            txtShowDept.text = item.department
            txtShowDOJ.text = item.date
            txtShowPass.text = item.password
        }
    }

}