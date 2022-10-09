package com.hackathon.cubicle.admin.ui.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.hackathon.cubicle.admin.ui.adapter.EachEmployeeAdapter
import com.hackathon.cubicle.data.EachEmployeeData
import com.hackathon.cubicle.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private val list = arrayListOf<EachEmployeeData>()
    private lateinit var binding: ActivityAdminBinding
    private lateinit var database: DatabaseReference

    val adapter = EachEmployeeAdapter(list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        database = FirebaseDatabase.getInstance().reference
        binding.empRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.empRV.adapter = adapter

        showTask()

        binding.fab.setOnClickListener{
            startActivity(Intent(this, AddEmployeeDialog::class.java))
        }
    }

    private fun showTask(){
        database.child("login").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val emp = ArrayList<EachEmployeeData>()
                for (data : DataSnapshot in snapshot.children){
                    data.getValue(EachEmployeeData::class.java)?.let{
                        if(it.As == "user")
                            emp.add(it)
                    }
                }
                list.clear()
                list.addAll(emp)
                Log.e("List", list.toString())
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}