package com.hackathon.cubicle.employee.ui.view.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hackathon.cubicle.R
import com.hackathon.cubicle.databinding.ActivityEmployeeBinding
import com.hackathon.cubicle.employee.ui.view.fragments.DashboardFragment
import com.hackathon.cubicle.employee.ui.view.fragments.ProfileFragment
import com.hackathon.cubicle.employee.ui.view.fragments.TaskFragment


class EmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeBinding
    private lateinit var userID :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.purple_400)
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_container, TaskFragment()).commit();

        binding.bottomNavigator.selectedItemId = R.id.nav_tasks
        binding.bottomNavigator.setOnItemSelectedListener {
            val id = it.itemId
            var fragment: Fragment? = null
            if(id == R.id.nav_tasks){
                fragment = TaskFragment()
            }else if(id == R.id.nav_dashboard){
                fragment = DashboardFragment()
            }else if(id == R.id.nav_profile){
                fragment = ProfileFragment()
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
            }
            true
        }

        userID = intent.getStringExtra("UserID")!!
    }

    fun getData():String {
        return userID
    }
}