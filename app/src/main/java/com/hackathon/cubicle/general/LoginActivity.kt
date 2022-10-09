package com.hackathon.cubicle.general

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hackathon.cubicle.R
import com.hackathon.cubicle.admin.ui.view.activity.AdminActivity
import com.hackathon.cubicle.employee.ui.view.activity.EmployeeActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.orange_400)
        }

        val editText_username =findViewById<EditText>(R.id.username)
        val editText_password =findViewById<EditText>(R.id.password)
        val login_button=findViewById<Button>(R.id.login_button)
        val switch: SwitchCompat =findViewById(R.id.RememberSwitch)

        login_button.setOnClickListener {

            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

            databaseReference.child("login")
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(datasnapshot: DataSnapshot) {

                        val username: String = editText_username.text.toString()
                        val password: String = editText_password.text.toString()

                        if (datasnapshot.child(username).exists()) {
                            if (datasnapshot.child(username).child("password").getValue(String::class.java).equals(password)) {
                                Log.d("check","present1")
                                if (switch.isChecked) {
                                    if (datasnapshot.child(username).child("as").getValue(String::class.java).equals("admin")) {
                                        preferences.setDataLogin(this@LoginActivity, true)
                                        preferences.setDataAs(this@LoginActivity, "admin")
                                        preferences.setUserID(this@LoginActivity, username)

                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                AdminActivity::class.java
                                            )
                                        )

                                        finish()

                                    } else if (datasnapshot.child(username).child("as").getValue(String::class.java).equals("user")) {
                                        preferences.setDataLogin(this@LoginActivity, true)
                                        preferences.setDataAs(this@LoginActivity, "user")
                                        preferences.setUserID(this@LoginActivity, username)

                                        val intent = Intent(this@LoginActivity, EmployeeActivity::class.java)
                                        intent.putExtra("UserID", username)
                                        startActivity(intent)

                                        finish()
                                    }
                                } else {
                                    if (datasnapshot.child(username).child("as").getValue(String::class.java).equals("admin")){
                                        preferences.setDataLogin(this@LoginActivity, false)

                                        startActivity(Intent(this@LoginActivity, AdminActivity::class.java))

                                        finish()
                                    } else if (datasnapshot.child(username).child("as").getValue(String::class.java).equals("user")) {
                                        preferences.setDataLogin(this@LoginActivity, false)

                                        val intent = Intent(this@LoginActivity, EmployeeActivity::class.java)
                                        intent.putExtra("UserID", username)
                                        startActivity(intent)

                                        finish()
                                    }

                                }
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Wrong Password",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "User Not Registered",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }


                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }


    }
    override fun onStart() {
        super.onStart()
        if (preferences.getDataLogin(this)){
            if (preferences.getDataAs(this).equals("admin")) {
                startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                finish()
            } else {
                val userId = preferences.getUserID(this@LoginActivity)
                val intent = Intent(this@LoginActivity, EmployeeActivity::class.java)
                intent.putExtra("UserID", userId)
                startActivity(intent)
                finish()
            }
        }
    }
}







