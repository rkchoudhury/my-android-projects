package com.example.androidarchitectures.mvp

import com.example.androidarchitectures.model.CountryService
import com.example.androidarchitectures.model.countryService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryPresenter(view: View) {
    private val view = view
    private val service: CountryService = countryService

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        scope.launch(Dispatchers.IO) {          // network work on IO
            try {
                val countries = service.getCountries()
                val countryNames = countries.map { it.countryName }
                withContext(Dispatchers.Main) {  // explicitly back to Main for UI
                    view.setValues(countryNames)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {  // same for error
                    view.onError()
                }
            }
        }
    }

    fun onRefresh() {
        fetchCountries()
    }

    // Call this to cancel coroutines when the Activity is destroyed
    fun onDestroy() {
        scope.cancel()
    }

    interface View {
        fun setValues(countries: List<String>)
        fun onError()
    }
}