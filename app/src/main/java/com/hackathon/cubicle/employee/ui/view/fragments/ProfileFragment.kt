package com.hackathon.cubicle.employee.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hackathon.cubicle.R
import com.hackathon.cubicle.data.EachEmployeeData
import com.hackathon.cubicle.databinding.FragmentAddTaskBinding
import com.hackathon.cubicle.databinding.FragmentProfileBinding
import com.hackathon.cubicle.employee.ui.view.activity.EmployeeActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!
    private lateinit var userID :String

    private val dbRef by lazy{
        FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val employeeActivity = activity as EmployeeActivity
        userID = employeeActivity.getData()

        dbRef.child("login").child(userID).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(EachEmployeeData::class.java)

                //getting header layout id from navigation view
                val userName = binding.tvUserName
                val phnNumber = binding.tvUserNo
                val dept = binding.tvUserDepartment
                val joining = binding.tvUserJoiningDate
                val email = binding.tvUserEmail

                userName.text = user?.name
                phnNumber.text = user?.number
                dept.text = user?.department
                joining.text = user?.date
                email.text = user?.mail
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return view
    }

}