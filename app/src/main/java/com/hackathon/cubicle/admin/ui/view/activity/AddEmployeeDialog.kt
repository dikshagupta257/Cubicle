package com.hackathon.cubicle.admin.ui.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.hackathon.cubicle.data.EachEmployeeData
import com.hackathon.cubicle.databinding.DialogAddEmployeeBinding

class AddEmployeeDialog: AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding:DialogAddEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddEmployeeBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        database = FirebaseDatabase.getInstance().reference

        binding.empAdd.setOnClickListener {
              val name = binding.empNameAdd.text.toString()
              val department = binding.empDepartmentAdd.text.toString()
              val mail = binding.empEmailAdd.text.toString()
              val number = binding.empPhoneAdd.text.toString()
              val date = binding.empJoinDate.text.toString()
              val password = binding.empPasswordAdd.text.toString()

              if(name.isEmpty() || department.isEmpty() || mail.isEmpty() || number.isEmpty() || date.isEmpty() || password.isEmpty()){
                  Toast.makeText(this, "Please Enter All The Information !!", Toast.LENGTH_SHORT).show()
                  return@setOnClickListener
              }
              val employee = EachEmployeeData(name, mail, number, department, date, password, "user")

                database.child("login").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(datasnapshot: DataSnapshot) {
                        if(datasnapshot.child(employee.number).exists()){
                            Toast.makeText(this@AddEmployeeDialog, "The user with the same phone number already exists!\n Please try with a new number.", Toast.LENGTH_SHORT).show()
                        }else{
                            saveEmployeesData(employee)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

        binding.empCancel.setOnClickListener{
            finish()
        }
    }

    private fun saveEmployeesData(employee: EachEmployeeData) {
        database.child("login").child(employee.number).setValue(employee).addOnSuccessListener{
            Toast.makeText(this, "Employee Added Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Some error occurred! Please try again.\nError: $it.message", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}