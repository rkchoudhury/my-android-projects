package com.example.movieappcleanarchitecture.presentation.models

import com.example.movieappcleanarchitecture.data.model.Movie

/**
 * Sealed interface representing the possible UI states for the Dashboard screen.
 *
 * WHY SEALED INTERFACE?
 * - The screen can only be in ONE state at a time (Loading OR Success OR Error).
 * - Eliminates impossible states — can't be Loading AND have an Error simultaneously.
 * - The compiler forces you to handle every state in `when` — you can't forget one.
 * - If you add a new state later (e.g., Empty), the compiler will show every place to update.
 *
 * NAMING CONVENTION (Google):
 * - Named after the screen: "DashboardUiState" (not "MovieUiState" or "MovieState").
 * - The sub-states (Loading, Success, Error) are just names you choose.
 *   You could also name them Fetching, MoviesLoaded, Failed — any clear name works.
 *   Loading/Success/Error is the most common convention in Android.
 *
 * WHAT DOES ": DashboardUiState" MEAN?
 * - It's NOT a return type. It means INHERITANCE — "this IS A DashboardUiState".
 * - Same syntax as: class MovieRepositoryImpl : MovieRepository
 * - "data object Loading : DashboardUiState" → Loading IS A type of DashboardUiState
 * - "data class Success : DashboardUiState"  → Success IS A type of DashboardUiState
 * - This is why you can assign any of them to a variable of type DashboardUiState:
 *     val state: DashboardUiState = DashboardUiState.Loading   // ✅ Loading is a DashboardUiState
 *     val state: DashboardUiState = DashboardUiState.Success() // ✅ Success is a DashboardUiState
 *     val state: DashboardUiState = "hello"                    // ❌ String is NOT a DashboardUiState
 *
 * HOW IT WORKS:
 * - "data object Loading"  → Holds NO data. It's just a label meaning "we're loading".
 * - "data class Success"   → Holds the movie list. Created as: Success(movies = listOf(...))
 * - "data class Error"     → Holds the error message. Created as: Error(message = "...")
 *
 * HOW DATA IS STORED (Success example):
 *   // In ViewModel — movies list is passed into Success and stored as a property:
 *   val movies = getPopularMovies()                       // [Movie1, Movie2, Movie3]
 *   _uiState.value = DashboardUiState.Success(movies)     // Creates Success with movies inside
 *
 *   // In Screen — extract movies from the Success object:
 *   is DashboardUiState.Success -> { state.movies }       // Gets back [Movie1, Movie2, Movie3]
 *
 *   // It's the same as any data class:
 *   data class Success(val movies: List<Movie>)
 *   val result = Success(movies)   // movies is stored in result.movies
 *
 * HOW TO SET STATE (in ViewModel):
 *   _uiState.value = DashboardUiState.Loading              // start loading
 *   _uiState.value = DashboardUiState.Success(movieList)   // got data
 *   _uiState.value = DashboardUiState.Error("Network err") // something failed
 *
 * HOW TO READ STATE (in Composable):
 *   when (val state = uiState) {
 *       is DashboardUiState.Loading -> { /* show spinner */ }
 *       is DashboardUiState.Success -> { MovieGrid(state.movies) }
 *       is DashboardUiState.Error   -> { MovieError(state.message) }
 *   }
 *
 * VS OLD DATA CLASS APPROACH:
 *   Old: data class MovieState(list, loading, error)  → all fields exist at once, can conflict
 *   New: sealed interface DashboardUiState            → only one state at a time, each with its own data
 *
 * Ref: https://developer.android.com/topic/architecture/ui-layer
 */
sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(val movies: List<Movie>) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}