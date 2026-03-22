package com.example.androidarchitectures.mvp

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
import com.example.androidarchitectures.R

class MVPActivity : AppCompatActivity(), CountryPresenter.View {

    private val listValues = ArrayList<String>()
    private lateinit var list: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var presenter: CountryPresenter
    private lateinit var retryButton: Button
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mvp)

        presenter = CountryPresenter(this)

        list = findViewById(R.id.list)
        retryButton = findViewById(R.id.retryButton)
        progress = findViewById(R.id.progress)
        adapter = ArrayAdapter(this, R.layout.row_layout, R.id.listText, listValues)

        list.setAdapter(adapter)
        list.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "You clicked " + listValues.get(position), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun setValues(countries: List<String>) {
        listValues.clear()
        listValues.addAll(countries)
        retryButton.visibility = View.GONE
        progress.visibility = View.GONE
        list.visibility = View.VISIBLE
        adapter.notifyDataSetChanged()
    }

    override fun onError() {
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
        progress.visibility = View.GONE
        list.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
    }

    fun onRetry(view: View) {
        presenter.onRefresh()
        list.visibility = View.GONE
        retryButton.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MVPActivity::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}