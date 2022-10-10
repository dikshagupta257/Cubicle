package com.hackathon.cubicle.admin.ui.view.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hackathon.cubicle.R
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.hackathon.cubicle.databinding.ActivityEmployeeDetailsBinding
import com.hackathon.cubicle.employee.data.model.TaskModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EmployeeDetailsActivity : AppCompatActivity() {
    private val list = arrayListOf<TaskModel>()
    private lateinit var userID :String
    private lateinit var binding: ActivityEmployeeDetailsBinding
    private val dbRef by lazy{
        FirebaseDatabase.getInstance().reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        userID = intent.getStringExtra("UserID").toString()
        showData()
    }

    private fun showData(){
        dbRef.child("Employee tasks").child(userID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = ArrayList<TaskModel>()
                for(data: DataSnapshot in snapshot.children){
                    data.getValue(TaskModel::class.java)?.let { temp.add(it) }
                }
                list.clear()
                list.addAll(temp)
                setDataCurr(list)
                setDataPrev(list)
                Log.e("List", list.toString())
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setDataCurr(tasks: List<TaskModel>) = with(binding){
        var workTime = 0L
        var meetingTime = 0L
        var breakTime = 0L

        for(task in tasks){
            if(task.category == "Break" && dateFormatter(task.date) == dateFormatter(Calendar.getInstance().time.time)){
                breakTime += task.timeTaken
            }else  if(task.category == "Meeting" && dateFormatter(task.date) == dateFormatter(Calendar.getInstance().time.time)){
                meetingTime += task.timeTaken
            }else if(task.category == "Work" && dateFormatter(task.date) == dateFormatter(Calendar.getInstance().time.time)){
                workTime += task.timeTaken
            }
        }

        val pieValues = mutableListOf<PieEntry>()
        if(breakTime > 0L) pieValues.add(PieEntry(breakTime.toFloat(), "Break"))
        if(meetingTime > 0L) pieValues.add(PieEntry(meetingTime.toFloat(), "Meeting"))
        if(workTime > 0L) pieValues.add(PieEntry(workTime.toFloat(), "Work"))

        val colors = listOf(
            ContextCompat.getColor(baseContext, R.color.red_500),
            ContextCompat.getColor(baseContext, R.color.orange_400),
            ContextCompat.getColor(baseContext, R.color.purple_400),
        )
        val dataSet = PieDataSet(pieValues, "Chart")
        dataSet.selectionShift = 3f
        dataSet.sliceSpace = 1f
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(ContextCompat.getColor(baseContext, R.color.white))

        binding.pieChartToday.apply {
            setData(data)
            invalidate()
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(true)
            setEntryLabelColor(ContextCompat.getColor(baseContext, R.color.white))
            animateY(1000, com.github.mikephil.charting.animation.Easing.EaseInCubic)
        }
    }

    private fun setDataPrev(tasks: List<TaskModel>) = with(binding){
        var workTimePrev = 0L
        var meetingTimePrev = 0L
        var breakTimePrev = 0L

        for(task in tasks){
            if(task.category == "Break" && dateFormatter(task.date) == dateFormatter(previousDate().time.time)){
                breakTimePrev += task.timeTaken
            }else  if(task.category == "Meeting" && dateFormatter(task.date) == dateFormatter(previousDate().time.time)){
                meetingTimePrev += task.timeTaken
            }else if(task.category == "Work" && dateFormatter(task.date) == dateFormatter(previousDate().time.time)){
                workTimePrev += task.timeTaken
            }
        }

        val pieValues = mutableListOf<PieEntry>()
        if(breakTimePrev > 0L) pieValues.add(PieEntry(breakTimePrev.toFloat(), "Break"))
        if(meetingTimePrev > 0L) pieValues.add(PieEntry(meetingTimePrev.toFloat(), "Meeting"))
        if(workTimePrev > 0L) pieValues.add(PieEntry(workTimePrev.toFloat(), "Work"))

        val colors = listOf(
            ContextCompat.getColor(baseContext, R.color.red_500),
            ContextCompat.getColor(baseContext, R.color.orange_400),
            ContextCompat.getColor(baseContext, R.color.purple_400),
        )
        val dataSet = PieDataSet(pieValues, "Chart")
        dataSet.selectionShift = 3f
        dataSet.sliceSpace = 1f
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(ContextCompat.getColor(baseContext, R.color.white))

        binding.pieChartYesterday.apply {
            setData(data)
            invalidate()
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(true)
            setEntryLabelColor(ContextCompat.getColor(baseContext, R.color.white))
            animateY(1000, com.github.mikephil.charting.animation.Easing.EaseInCubic)
        }
    }

    private fun dateFormatter(date: Long): String{
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        val dateString = sdf.format(date)
        return dateString
    }

    private fun previousDate(): Calendar {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return cal
    }
}