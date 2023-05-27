package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.jraska.console.Console

class RuntimeConsole : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_runtime_console)
        supportActionBar?.hide()
        val clear_btn = findViewById<Button>(R.id.clear_button)
        clear_btn.setOnClickListener{
            Console.clear()
        }
    }
}