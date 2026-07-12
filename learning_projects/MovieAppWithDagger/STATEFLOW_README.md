# StateFlow Version — Presentation Layer

> **When to use this:** Once you're comfortable with Kotlin Flow/StateFlow concepts.
> These files replace `mutableStateOf` with `StateFlow` in the ViewModel.
> The UI uses `collectAsState()` to observe the state.

---

## Why StateFlow?

| `mutableStateOf` (current) | `StateFlow` (this version) |
|---|---|
| Compose-only | Works anywhere (Compose, XML, tests) |
| No lifecycle awareness | `collectAsState()` respects lifecycle |
| Can't test without Compose | Testable with plain JUnit |

---

## File 1: `DashboardUiState.kt`

**Location:** `presentation/screens/dashboard/DashboardUiState.kt`

```kotlin
package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import com.example.movieappcleanarchitecture.data.model.Movie

/**
 * Sealed interface representing the possible UI states for the Dashboard screen.
 * Named after the screen it belongs to (Google convention).
 *
 * Using a sealed interface eliminates impossible states —
 * the screen can only be in ONE of these states at a time.
 */
sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(val movies: List<Movie>) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
```

---

## File 2: `DashboardViewModel.kt`

**Location:** `presentation/screens/dashboard/DashboardViewModel.kt`

```kotlin
package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.data.remote.MovieRemoteDataSource
import com.example.movieappcleanarchitecture.data.repository.MovieRepositoryImpl
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * State holder for the Dashboard screen.
 * Named after the screen it serves (Google convention).
 * Uses StateFlow to expose UI state — framework-agnostic and lifecycle-aware.
 */
class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val movieRepository = MovieRepositoryImpl(MovieRemoteDataSource())
    private val getPopularMovies = GetPopularMoviesUseCase(movieRepository)

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val movies = getPopularMovies()
                _uiState.value = DashboardUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun retry() {
        fetchMovies()
    }
}
```

---

## File 3: `DashboardScreen.kt`

**Location:** `presentation/screens/dashboard/DashboardScreen.kt`

```kotlin
package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieappcleanarchitecture.presentation.components.LoadingIndicator
import com.example.movieappcleanarchitecture.presentation.components.MovieError
import com.example.movieappcleanarchitecture.presentation.components.MovieGrid

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = uiState) {
            is DashboardUiState.Loading -> {
                LoadingIndicator()
            }

            is DashboardUiState.Error -> {
                MovieError("Something went wrong\n" + state.message)
            }

            is DashboardUiState.Success -> {
                MovieGrid(state.movies)
            }
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}
```

---

## Key Differences from mutableStateOf version

### ViewModel
```
BEFORE:  private val _moviesState = mutableStateOf(MovieState())
AFTER:   private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)

BEFORE:  val moviesState: State<MovieState> = _moviesState
AFTER:   val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
```

### Screen
```
BEFORE:  val movieList by viewModel.moviesState
AFTER:   val uiState by viewModel.uiState.collectAsState()

BEFORE:  when { movieList.loading -> ... }
AFTER:   when (val state = uiState) { is DashboardUiState.Loading -> ... }
```

### State Model
```
BEFORE:  data class MovieState(val list: ..., val loading: ..., val error: ...)
AFTER:   sealed interface DashboardUiState { Loading, Success(movies), Error(message) }
```
