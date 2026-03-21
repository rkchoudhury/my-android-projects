package com.example.androidarchitectures.mvc

import android.util.Log
import com.example.androidarchitectures.model.CountryService
import com.example.androidarchitectures.model.countryService
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

class CountryController(mvcActivity: MVCActivity) {

    private var view: MVCActivity = mvcActivity
    private var service: CountryService = countryService

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
}