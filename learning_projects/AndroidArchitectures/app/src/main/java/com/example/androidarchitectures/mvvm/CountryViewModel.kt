package com.example.androidarchitectures.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidarchitectures.model.CountryService
import com.example.androidarchitectures.model.countryService
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {

    private val countries: MutableLiveData<List<String>> = MutableLiveData()
    private val countryError: MutableLiveData<Boolean> = MutableLiveData()

    private val service: CountryService = countryService

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            try {
                val response = service.getCountries()
                val countryNames = response.map { it.countryName }
                countries.value = countryNames
                countryError.value = false
            } catch (e: Exception) {
                countryError.value = true
            }
        }
    }

    fun onRefresh() {
        fetchCountries()
    }

    fun getCountries(): LiveData<List<String>> {
        return countries
    }

    fun getCountryError(): LiveData<Boolean> {
        return countryError
    }
}