package com.example.androidarchitectures.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidarchitectures.R

class MVCActivity : AppCompatActivity() {

    private val listValues = ArrayList<String>()
    private lateinit var list: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var controller: CountryController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mvc)

        controller = CountryController(this)

        list = findViewById(R.id.list)
        adapter = ArrayAdapter(this, R.layout.row_layout, R.id.listText, listValues)

        list.setAdapter(adapter)
        list.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "You clicked " + listValues.get(position), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun setValues(values: List<String>) {
        listValues.clear()
        listValues.addAll(values)
        adapter.notifyDataSetChanged()
    }

    fun onError() {

    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MVCActivity::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.onDestroy()
    }
}