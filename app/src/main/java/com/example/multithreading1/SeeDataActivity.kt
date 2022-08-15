package com.example.multithreading1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SeeDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_data)

        findViewById<TextView>(R.id.textView).text = intent.extras?.getString(SAVED_DATA_KEY) ?: "No data"
    }
}