package com.example.recipeapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ViewModel takes care the communication between the data and ui
// Prepare everything so that the ui can work with it
class MainViewModel: ViewModel() {
    // This is the structure of our state to which we will update the value
    data class RecipeState(
        val list: List<Category> = emptyList(),
        val loading: Boolean = true,
        val error: String? = null
    )

    // This is the private state variable which is initialized with RecipeState
    // Whenever the _categoriesState value changes/updates it will trigger recomposition
    private val _categoriesState = mutableStateOf(RecipeState())

    // This is the public variable which can be accessed from the outside
    val categoriesState: State<RecipeState> = _categoriesState

    // On object creation we are fetching the categories
    init {
        fetchCategories()
    }

    // viewModelScope provides a scope for suspend function to be processed
    // A suspend function is a function that runs in the background without blocking the main thread
    // To start a suspend function we have to start inside a co-routine
    // Here viewModelScope will create a coroutine scope
    // Basically coroutine will allow you to run a routine in the background
    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = recipeService.getCategories()
                _categoriesState.value = _categoriesState.value.copy(
                    list = response.categories.filter { item -> item.idCategory != "1" },
//                    list = response.categories,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(
                    loading = false,
                    error = "Error fetching categories ${e.cause} - ${e.message}"
                )
            }
        }
    }
}