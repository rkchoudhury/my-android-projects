package com.example.androidarchitectures.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.androidarchitectures.R

class MVVMActivity : AppCompatActivity() {

    private val listValues = ArrayList<String>()
    private lateinit var list: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var viewModel: CountryViewModel
    private lateinit var retryButton: Button
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mvvm)

        viewModel = ViewModelProvider(this)[CountryViewModel::class.java]

        list = findViewById(R.id.list)
        retryButton = findViewById(R.id.retryButton)
        progress = findViewById(R.id.progress)
        adapter = ArrayAdapter(this, R.layout.row_layout, R.id.listText, listValues)

        list.adapter = adapter
        list.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "You clicked " + listValues.get(position), Toast.LENGTH_SHORT)
                .show()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getCountries().observe(this) { countries ->
            if (countries != null) {
                listValues.clear()
                listValues.addAll(countries)
                list.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            } else {
                list.visibility = View.GONE
            }
        }

        viewModel.getCountryError().observe(this) { error ->
            progress.visibility = View.GONE
            if (error) {
                Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
                list.visibility = View.GONE
                retryButton.visibility = View.VISIBLE
            } else {
                retryButton.visibility = View.GONE
            }
        }
    }

    fun onRetry(view: View) {
        viewModel.onRefresh()
        list.visibility = View.GONE
        retryButton.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MVVMActivity::class.java)
        }
    }
}