package com.example.androidarchitectures.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidarchitectures.R

class MVCActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mvc)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MVCActivity::class.java)
        }
    }
}