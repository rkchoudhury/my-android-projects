package com.example.androidarchitectures.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidarchitectures.model.CountryService
import com.example.androidarchitectures.model.countryService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryViewModel: ViewModel() {

    private val countries: MutableLiveData<List<String>> = MutableLiveData()
    private val countryError: MutableLiveData<Boolean> = MutableLiveData()

    private val service: CountryService = countryService

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        scope.launch(Dispatchers.IO) {          // network work on IO
            try {
                val response = service.getCountries()
                val countryNames = response.map { it.countryName }
                withContext(Dispatchers.Main) {  // explicitly back to Main for UI
                    countries.value = countryNames
                    countryError.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {  // same for error
                    countryError.value = true
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

    fun getCountries(): LiveData<List<String>> {
        return countries
    }

    fun getCountryError(): LiveData<Boolean> {
        return countryError
    }
}