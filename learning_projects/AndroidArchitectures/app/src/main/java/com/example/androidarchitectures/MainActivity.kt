package com.example.androidarchitectures

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidarchitectures.mvc.MVCActivity
import com.example.androidarchitectures.mvp.MVPActivity
import com.example.androidarchitectures.mvvm.MVVMActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    fun onMVC(view: View) {
        startActivity(MVCActivity.getIntent(this))
    }

    fun onMVP(view: View) {
        startActivity(MVPActivity.getIntent(this))
    }

    fun onMVVM(view: View) {
        startActivity(MVVMActivity.getIntent(this))
    }

}