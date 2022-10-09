package com.hackathon.cubicle.employee.ui.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.hackathon.cubicle.R
import com.hackathon.cubicle.databinding.FragmentAddTaskBinding
import com.hackathon.cubicle.employee.data.model.TaskModel
import java.text.SimpleDateFormat
import java.util.*

class AddTaskFragment : Fragment(), View.OnClickListener  {
    private lateinit var navBar:BottomNavigationView
    private var _binding:FragmentAddTaskBinding?=null
    private val binding get() = _binding!!


    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var myCalendar: Calendar
    private lateinit var dateSetListener : DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener : TimePickerDialog.OnTimeSetListener
    private val labels = arrayListOf("Break" , "Meeting" , "Work")
    private var finalDate = 0L
    private var finalTime = 0L

    private val dbRef by lazy{
        FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        navBar = activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigator);
        navBar.visibility = View.GONE

        binding.dateEdt.setOnClickListener(this)
        binding.timeEdt.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)
        setUpSpinner()
        return view
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.dateEdt -> setDateListener()
            R.id.timeEdt -> setTimeListener()
            R.id.saveBtn -> saveTask()
        }
    }

    private fun setUpSpinner() {
        spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, labels)

        labels.sort()
        binding.spinnerCategory.adapter = spinnerAdapter
    }

    private fun saveTask() {
        val category: String = binding.spinnerCategory.selectedItem.toString()
        val title = binding.titleInpLay.editText?.text.toString()
        val description = binding.taskInpLay.editText?.text.toString()
        val timeDuration = binding.timeDurInpLay.editText?.text.toString()
        val fm: FragmentManager = activity!!.supportFragmentManager

        when {
            title.isEmpty() -> {
                binding.titleInpLay.error = "Title cannot be empty!"
            }
            description.isEmpty() -> {
                binding.taskInpLay.error = "Task cannot be empty!"
            }
            finalDate == 0L -> {
                binding.dateInpLay.error = "Please set a date."
            }
            finalTime == 0L -> {
                binding.timeInpLay.error = "Please set a time."
            }
            category.isEmpty() -> {
                Toast.makeText(activity!!.applicationContext,"Please select a category",Toast.LENGTH_SHORT).show()
            }
            timeDuration.isEmpty() -> {
                binding.timeDurInpLay.error = "Please set a time duration of the task"
            }
            else -> {

                binding.saveBtn.isEnabled = false
                //add to firebase
                val task = TaskModel(title, description, category, finalDate, finalTime, timeDuration.toLong())
                val id = dbRef.child("Employee tasks").push().key!!
                dbRef.child("Employee tasks").child(id).setValue(task)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Task added successfully!", Toast.LENGTH_SHORT).show()
                        binding.saveBtn.isEnabled = true
                        fm.popBackStack()
                    }
                    .addOnFailureListener {
                        AlertDialog.Builder(requireContext())
                            .setMessage("Some error occurred.Please try again.\nError: $it")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()

                        binding.saveBtn.isEnabled = true
                    }

            }
        }
    }

    //to select date selected from DatePickerDialog
    private fun setDateListener() {
        myCalendar = Calendar.getInstance()
        dateSetListener = DatePickerDialog.OnDateSetListener{ _ : DatePicker, year :Int, month:Int, dayOfMonth:Int ->
            myCalendar.set(Calendar.YEAR , year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH , dayOfMonth)
            updateDate()
        }

        //date picker dialog box
        val datePickerDialog = DatePickerDialog(requireContext() , dateSetListener ,
            myCalendar.get(Calendar.YEAR) , myCalendar.get(Calendar.MONTH) , myCalendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    //set date of task according to fn setDateListener
    @SuppressLint("SimpleDateFormat")
    private fun updateDate() {
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)  //simple date format
        binding.dateEdt.setText(sdf.format(myCalendar.time))
        finalDate = myCalendar.time.time
        binding.timeInpLay.visibility = View.VISIBLE
    }

    //to select time selected from TimePickerDialog
    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()
        timeSetListener = TimePickerDialog.OnTimeSetListener{ _ : TimePicker, hourOfDay : Int, min:Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY , hourOfDay)
            myCalendar.set(Calendar.MINUTE , min)
            updateTime()
        }

        //time picker dialog box
        val timePickerDialog = TimePickerDialog(
            requireContext(), timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    //set time of task according to fn setTimeListener
    @SuppressLint("SimpleDateFormat")
    private fun updateTime() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat)
        binding.timeEdt.setText(sdf.format(myCalendar.time))
        finalTime = myCalendar.time.time
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navBar.visibility = View.VISIBLE
    }

}