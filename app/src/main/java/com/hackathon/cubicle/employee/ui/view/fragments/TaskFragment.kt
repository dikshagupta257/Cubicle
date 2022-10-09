package com.hackathon.cubicle.employee.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hackathon.cubicle.databinding.FragmentTaskBinding
import com.hackathon.cubicle.employee.data.model.TaskModel
import com.hackathon.cubicle.employee.ui.adapter.TaskAdapter
import com.hackathon.cubicle.employee.ui.view.activity.EmployeeActivity

class TaskFragment : Fragment() {
    private val list = arrayListOf<TaskModel>()
    private lateinit var taskAdapter : TaskAdapter
    private var _binding:FragmentTaskBinding?=null
    private val binding get() = _binding!!
    private lateinit var userID :String

    private val dbRef by lazy{
        FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        taskAdapter = TaskAdapter(list)
        binding.taskRV.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = taskAdapter
        }

        showTasks()

        binding.addTaskBtn.setOnClickListener {
            val addTaskFragment = AddTaskFragment()
            val fragmentManager:FragmentManager? = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace((getView()!!.parent as ViewGroup).id, addTaskFragment)
                ?.addToBackStack(null)?.commit()

        }
        return view
    }

    private fun showTasks() {
        //notify adapter and update list by taking data from firebase
        val employeeActivity = activity as EmployeeActivity
        userID = employeeActivity.getData()

        dbRef.child("Employee tasks").child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = ArrayList<TaskModel>()
                for(data: DataSnapshot in snapshot.children){
                    data.getValue(TaskModel::class.java)?.let { temp.add(it) }
                }
                list.clear()
                list.addAll(temp)
                Log.e("List", list.toString())
                taskAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                //not needed
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}