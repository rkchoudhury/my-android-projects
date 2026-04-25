# MovieApp — Clean Architecture

An Android app that fetches and displays popular movies from the [TMDB API](https://www.themoviedb.org/documentation/api),
built with **Jetpack Compose** following **Google's recommended app architecture**.

---

## Tech Stack

| Tool           | Purpose                          |
|----------------|----------------------------------|
| Kotlin         | Language                         |
| Jetpack Compose| Declarative UI                   |
| Retrofit 3.0   | HTTP client for TMDB API         |
| Gson           | JSON parsing                     |
| Coil           | Image loading (poster images)    |
| Coroutines     | Async network calls              |
| ViewModel      | UI state management              |

---

## Architecture

Follows [Google's official app architecture](https://developer.android.com/topic/architecture) with 3 layers:

```
┌─────────────────────────────────────────┐
│           UI LAYER (presentation/)      │  Screens, ViewModels, UI State
│                                         │  Observes state, renders UI
├─────────────────────────────────────────┤
│         DOMAIN LAYER (domain/)          │  Use Cases only
│                                         │  Optional business logic layer
├─────────────────────────────────────────┤
│          DATA LAYER (data/)             │  Repositories, Data Sources, Models
│                                         │  Single source of truth for data
└─────────────────────────────────────────┘
```

**Data flow:**
```
API JSON → MovieApiModel (15 fields) → Repository maps → Movie (7 fields) → UseCase → ViewModel → Screen
```

---

## Project Structure

```
com.example.movieappcleanarchitecture/
│
├── common/                                    ── SHARED CONSTANTS
│   └── Constants.kt                           API_BASE_URL, CDN_IMAGE_URL
│
├── data/                                      ── DATA LAYER
│   ├── model/
│   │   └── Movie.kt                           Business model (7 clean fields, NO @SerializedName)
│   │                                           Exposed to domain & UI layers
│   │
│   ├── remote/
│   │   ├── BaseRemoteDataSource.kt             Abstract base class — shared Retrofit instance
│   │   ├── MovieApiService.kt                  Retrofit interface (API endpoints)
│   │   ├── MovieRemoteDataSource.kt            Wraps API calls, extends BaseRemoteDataSource
│   │   └── model/
│   │       ├── MovieApiModel.kt                Raw API model (all 15 fields, @SerializedName)
│   │       └── MovieApiResponse.kt             API wrapper: { results: List<MovieApiModel> }
│   │                                           ↑ Internal to data layer, NOT exposed
│   │
│   └── repository/
│       ├── MovieRepository.kt                  Interface: returns List<Movie>
│       └── MovieRepositoryImpl.kt              Maps MovieApiModel → Movie
│
├── domain/                                    ── DOMAIN LAYER
│   └── usecase/
│       └── GetPopularMoviesUseCase.kt          Single responsibility use case
│                                               operator fun invoke() → List<Movie>
│
├── presentation/                              ── UI LAYER
│   ├── MainActivity.kt                         App entry point
│   │
│   ├── models/
│   │   └── DashboardUiState.kt                 Sealed interface: Loading | Success | Error
│   │
│   ├── screens/
│   │   └── dashboard/
│   │       ├── DashboardScreen.kt              Composable — renders based on UiState
│   │       └── DashboardViewModel.kt           Manages UI state, calls use case
│   │
│   └── components/
│       ├── LoadingIndicator.kt                 Reusable loading spinner
│       ├── MovieCard.kt                        Single movie card composable
│       ├── MovieGrid.kt                        Grid layout of movie cards
│       └── MovieError.kt                       Error message composable
│
└── ui/theme/                                  ── COMPOSE THEME
    ├── Color.kt                                Color definitions
    ├── Theme.kt                                Material theme setup
    └── Type.kt                                 Typography
```

---

## Layer Details

### Data Layer (`data/`)
- **Owns all data operations** — API calls, mapping, caching (future)
- `MovieApiModel` has all 15 API fields with `@SerializedName` (internal, not exposed)
- `Movie` is the clean business model with only 7 relevant fields (exposed to other layers)
- `MovieRepositoryImpl` maps `MovieApiModel → Movie` (keeps API details private)
- `BaseRemoteDataSource` provides a shared Retrofit instance via `companion object` + `by lazy`

### Domain Layer (`domain/`)
- **Contains only use cases** — no models, no repository interfaces
- Each use case has a single `operator fun invoke()` method
- Sits between UI and Data layers
- Intentionally thin per Google's guidelines

### UI Layer (`presentation/`)
- **Screens** observe `DashboardUiState` from the ViewModel
- `DashboardUiState` is a **sealed interface** — the screen can only be in ONE state at a time:
  - `Loading` → show spinner
  - `Success(movies)` → show movie grid
  - `Error(message)` → show error message
- Uses `mutableStateOf` for Compose state management

---

## Setup

1. Get a free API key from [TMDB](https://www.themoviedb.org/settings/api)
2. Add your token to `gradle.properties`:
   ```properties
   TMDB_TOKEN=your_bearer_token_here
   ```
3. Build and run the app

---

## References

- [Google — Guide to app architecture](https://developer.android.com/topic/architecture)
- [Google — UI layer](https://developer.android.com/topic/architecture/ui-layer)
- [Google — Domain layer](https://developer.android.com/topic/architecture/domain-layer)
- [Google — Data layer](https://developer.android.com/topic/architecture/data-layer)
