package com.example.roomdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mDb:RoomSingleton
    private val mRandom:Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the room database
        mDb = RoomSingleton.getInstance(applicationContext)

        // Make the text view scrollable
        textView.movementMethod = ScrollingMovementMethod()

        // Insert button click listener
        btnInsert.setOnClickListener{
            val name = edtName.text.toString().trim()
            val age = edtAge.text.toString().trim()
            if(!name.isNullOrEmpty()){
                if(!age.isNullOrEmpty()){
                    val student = Student(id = null,
                        fullName = name,
                        result = age.toInt()
                    )

                    doAsync {
                        // Put the student in database
                        mDb.studentDao().insert(student)

                        uiThread {
                            toast("One record inserted.")
                        }
                    }
                }else{
                    toast("age can't be empty.")
                }
            }else{
                toast("name can't be empty.")
            }
            // Initialize a new student
        }


        // Select button click listener
        btnSelect.setOnClickListener{
            doAsync {
                // Get the student list from database
                val list = mDb.studentDao().allStudents()

                uiThread {
                    toast("${list.size} records found.")
                    // Display the students in text view
                    textView.text = ""
                    for (student in list){
                        textView.append("${student.id} : ${student.fullName} : ${student.result}\n")
                    }
                }
            }
        }
    }
}