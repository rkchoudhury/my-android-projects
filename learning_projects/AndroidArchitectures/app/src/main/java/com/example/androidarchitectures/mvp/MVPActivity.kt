package com.example.androidarchitectures.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidarchitectures.R

class MVPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mvp)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MVPActivity::class.java)
        }
    }
}