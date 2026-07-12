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

## Dependency Injection with Hilt

This project uses **Hilt** (built on Dagger) with **KSP** for dependency injection. Hilt removes manual component wiring and gives Android-aware entry points for `Application`, `Activity`, and `ViewModel`.

### Why Hilt?

Without DI, the ViewModel would manually create its own dependencies:

```kotlin
// ❌ Without DI — ViewModel knows HOW to build everything
val remoteDS = MovieRemoteDataSource()
val localDS = MovieLocalDataSource(context)
val repo = MovieRepositoryImpl(remoteDS, localDS)
val useCase = GetPopularMoviesUseCase(repo)
```

With Hilt, the ViewModel declares what it needs in the constructor and Hilt provides it:

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel()
```

---

### Hilt Setup Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                   MovieApplication (Application)                    │
│                        @HiltAndroidApp                              │
│          Creates the app-level Hilt container automatically         │
└─────────────────────────────────────────┬───────────────────────────┘
                                          │ installs
                                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       AppModule (DI Module)                         │
│        @Module + @InstallIn(SingletonComponent::class)             │
│                                                                      │
│  @Provides provideMovieRemoteDataSource() → MovieRemoteDataSource    │
│  @Provides provideMovieLocalDataSource(@ApplicationContext ctx)      │
│  @Provides provideMovieRepository(remote, local) → MovieRepository   │
│  @Provides provideGetPopularMoviesUseCase(repo)                      │
└─────────────────────────────────────────┬───────────────────────────┘
                                          │ injects
                                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│                MainActivity + DashboardViewModel                    │
│   @AndroidEntryPoint + @HiltViewModel + hiltViewModel()            │
└─────────────────────────────────────────────────────────────────────┘
```

---

### How Dependency Resolution Works

When `DashboardScreen` calls `hiltViewModel()`, Hilt creates `DashboardViewModel` and resolves:

1. `GetPopularMoviesUseCase`
2. `MovieRepository`
3. `MovieRemoteDataSource`
4. `MovieLocalDataSource`
5. `Context` from `@ApplicationContext`

Result: the full chain is built automatically and injected into the ViewModel constructor.

---

### File-by-File Breakdown

#### 1. `MovieApplication.kt` — App-level Hilt container

```kotlin
@HiltAndroidApp
class MovieApplication : Application()
```

#### 2. `di/AppModule.kt` — Providers installed in singleton graph

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMovieRemoteDataSource(): MovieRemoteDataSource = MovieRemoteDataSource()

    @Provides
    fun provideMovieLocalDataSource(
        @ApplicationContext context: Context
    ): MovieLocalDataSource = MovieLocalDataSource(context)

    @Provides
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MovieRepository = MovieRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    fun provideGetPopularMoviesUseCase(
        movieRepository: MovieRepository
    ): GetPopularMoviesUseCase = GetPopularMoviesUseCase(movieRepository)
}
```

#### 3. `presentation/MainActivity.kt` — Android entry point

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

#### 4. `DashboardViewModel.kt` + `DashboardScreen.kt` — injected ViewModel

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel()
```

```kotlin
@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()
    // ...
}
```

---

### Key Hilt Annotations

| Annotation | Purpose |
|-----------|---------|
| `@HiltAndroidApp` | Generates and initializes the app-level Hilt container |
| `@Module` | Declares provider methods for dependencies |
| `@InstallIn(SingletonComponent::class)` | Installs module in app-wide singleton graph |
| `@Provides` | Creates/returns a dependency instance |
| `@AndroidEntryPoint` | Enables Hilt injection in Android classes (Activity/Fragment/etc.) |
| `@HiltViewModel` | Enables constructor injection for ViewModel |
| `@ApplicationContext` | Qualifier for application `Context` |

---

### Gradle Setup

```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.hilt.android)
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}
```

---

### Manual DI vs Hilt Comparison

| Aspect | Manual DI (AppContainer) | Hilt |
|--------|---------------------------|------|
| Object creation | You write it yourself | Generated from modules + annotations |
| Wiring dependencies | Manual constructor chaining | Graph resolution at compile time |
| Android lifecycle integration | Manual | Built-in (`@AndroidEntryPoint`, `@HiltViewModel`) |
| Accessing dependencies in ViewModel | Pull from app container | Constructor injection |
| Boilerplate | Grows with app size | Lower and scalable |

---

## Scaling: Feature-Based Packaging

The current flat structure (`data/remote/`, `data/repository/`, `data/model/`) works well for a small app
with one feature. But as the app grows (movies, actors, reviews, search…), each folder fills up with
unrelated files and becomes hard to navigate.

**The solution: group by feature instead of by type.**

### Current Structure (flat — good for 1 feature)

```
data/
├── model/
│   └── Movie.kt
├── remote/
│   ├── BaseRemoteDataSource.kt
│   ├── MovieApiService.kt
│   ├── MovieRemoteDataSource.kt
│   └── model/
│       ├── MovieApiModel.kt
│       └── MovieApiResponse.kt
└── repository/
    ├── MovieRepository.kt
    └── MovieRepositoryImpl.kt
```

### Scaled Structure (feature-based — when app grows)

```
data/
├── movie/                              ← Everything movie-related in one place
│   ├── MovieRepository.kt                 Interface
│   ├── MovieRepositoryImpl.kt             Implementation (maps API → business model)
│   ├── MovieRemoteDataSource.kt           Wraps Retrofit calls
│   ├── MovieApiService.kt                 Retrofit interface
│   └── model/
│       ├── Movie.kt                        Business model (exposed to other layers)
│       ├── MovieApiModel.kt                API model with @SerializedName (internal)
│       └── MovieApiResponse.kt             API wrapper (internal)
│
├── actor/                              ← Everything actor-related in one place
│   ├── ActorRepository.kt
│   ├── ActorRepositoryImpl.kt
│   ├── ActorRemoteDataSource.kt
│   ├── ActorApiService.kt
│   └── model/
│       ├── Actor.kt                        Business model
│       └── ActorApiModel.kt                API model
│
└── remote/                             ← Shared networking base
    └── BaseRemoteDataSource.kt             Shared Retrofit instance (used by all data sources)
```

### Why Feature-Based?

| Flat (by type)                              | Feature-based                                |
|---------------------------------------------|----------------------------------------------|
| `remote/` has MovieApiService + ActorApiService + ... | `movie/` has everything for movies together |
| Must jump across 3 folders to understand one feature | Open one folder to see the full picture     |
| Works for 1-2 features                      | Scales to 10+ features cleanly               |

### When to Switch?

- **1 feature (now):** Keep the flat structure. Don't over-organize.
- **2+ features:** Consider grouping by feature.
- **The `remote/` folder** stays at the top level for shared code like `BaseRemoteDataSource`.

### Naming Convention

| Type         | Pattern                          | Example                    |
|--------------|----------------------------------|----------------------------|
| Business model | `{Feature}`                    | `Movie`, `Actor`           |
| API model    | `{Feature}ApiModel`              | `MovieApiModel`            |
| API response | `{Feature}ApiResponse`           | `MovieApiResponse`         |
| API service  | `{Feature}ApiService`            | `MovieApiService`          |
| Data source  | `{Feature}RemoteDataSource`      | `MovieRemoteDataSource`    |
| Repository   | `{Feature}Repository`            | `MovieRepository` (interface) |
| Repository impl | `{Feature}RepositoryImpl`     | `MovieRepositoryImpl`      |
| Use case     | `{Verb}{Feature}UseCase`         | `GetPopularMoviesUseCase`  |
| ViewModel    | `{Screen}ViewModel`              | `DashboardViewModel`       |
| UI state     | `{Screen}UiState`                | `DashboardUiState`         |

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
