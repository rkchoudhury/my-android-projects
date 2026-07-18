# Dagger vs Hilt

A concrete code comparison showing how PR #17 implements **Dagger 2** vs. the workspace's **Hilt** approach,
with actual code from both implementations.

---

## 1. Application Class

### Dagger (~8 lines, manual component setup)

```kotlin
// MovieApplication.kt
class MovieApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)  // ← manual initialization
    }
}
```

**Requires:** `AppComponent.kt` file + Dagger generates `DaggerAppComponent` class

### Hilt — Workspace Implementation (~2 lines)

```kotlin
// MovieApplication.kt
@HiltAndroidApp
class MovieApplication : Application()
```

**Advantage:** Hilt auto-generates everything, no manual factory needed, ties to app lifecycle automatically.

**Disadvantage (Dagger):** More boilerplate, but more explicit control.

---

## 2. Component Definition

### Dagger (explicit interface)

```kotlin
// di/AppComponent.kt
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getPopularMoviesUseCase(): GetPopularMoviesUseCase  // ← exposure method
    
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent  // ← context binding
    }
}
```

**You write:** 
- `@Component` interface
- `@Component.Factory` with `@BindsInstance`
- Exposure methods for what you need

**Dagger generates:**
- `DaggerAppComponent` class at build time

### Hilt — Workspace Implementation (auto-generated)

```
FILE DOES NOT EXIST — Hilt generates SingletonComponent automatically
```

**You don't write:** 
- No component interface
- No factory pattern
- No exposure methods

**Hilt generates:**
- Component wired to Android lifecycle
- `@ApplicationContext` built-in
- Automatic injection for `@AndroidEntryPoint` classes

**Advantage (Hilt):** Zero boilerplate for component setup  
**Advantage (Dagger):** Complete control, explicit about what's exposed

---

## 3. AppModule — Dependency Providers

### Dagger (plain @Module)

```kotlin
// di/AppModule.kt
@Module
class AppModule {

    @Provides
    fun provideMovieRemoteDataSource(): MovieRemoteDataSource {
        return MovieRemoteDataSource()
    }

    @Provides
    fun provideMovieLocalDataSource(context: Context): MovieLocalDataSource {
        return MovieLocalDataSource(context)
    }

    @Provides
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MovieRepository {
        return MovieRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    fun provideGetPopularMoviesUseCase(movieRepository: MovieRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(movieRepository)
    }
}
```

**Key points:**
- Plain `@Module` (no `@InstallIn`)
- Context passed as parameter (manual wiring)
- Methods included in `@Component(modules = [AppModule::class])`

### Hilt — Workspace Implementation (@InstallIn scope)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMovieRemoteDataSource(): MovieRemoteDataSource {
        return MovieRemoteDataSource()
    }

    @Provides
    fun provideMovieLocalDataSource(
        @ApplicationContext context: Context  // ← built-in qualifier
    ): MovieLocalDataSource {
        return MovieLocalDataSource(context)
    }

    @Provides
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MovieRepository {
        return MovieRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    fun provideGetPopularMoviesUseCase(movieRepository: MovieRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(movieRepository)
    }
}
```

**Key differences:**
- `@InstallIn(SingletonComponent::class)` = replaces `@Component(modules=[...])`
- `@ApplicationContext` qualifier = Hilt provides Context automatically
- No component wiring needed

**Advantage (Dagger):** You control everything explicitly  
**Advantage (Hilt):** `@ApplicationContext` is provided, auto-scoped to singleton

---

## 4. MainActivity

### Dagger (plain Activity)

```kotlin
// presentation/MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DashboardScreen() }
    }
}
```

**Key points:**
- No special annotations
- No dependency injection at Activity level
- ViewModel injection happens in Composable via component access

### Hilt — Workspace Implementation (@AndroidEntryPoint)

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DashboardScreen() }
    }
}
```

**Key difference:**
- `@AndroidEntryPoint` annotation
- Hilt automatically provides an `ActivityComponent` scoped to this Activity

**Advantage (Dagger):** No magic, Activity remains plain  
**Advantage (Hilt):** Can inject Activity-scoped dependencies automatically

---

## 5. DashboardViewModel

### Dagger (manual component access)

```kotlin
// presentation/screens/dashboard/DashboardViewModel.kt
class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val getPopularMovies = 
        (application as MovieApplication).appComponent.getPopularMoviesUseCase()

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
                _uiState.value = DashboardUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    // ... rest of implementation
}
```

**Key points:**
- Extends `AndroidViewModel` (holds Application reference)
- Manually accesses component: `(application as MovieApplication).appComponent`
- Calls exposed method: `.getPopularMoviesUseCase()`
- No factory needed, constructor is plain

### Hilt — Workspace Implementation (@HiltViewModel + hiltViewModel())

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
    val uiState: State<DashboardUiState> = _uiState

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
                _uiState.value = DashboardUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    // ... rest of implementation
}
```

And in the Composable:

```kotlin
@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()  // ← automatic factory generation
    val uiState by viewModel.uiState
    // render UI
}
```

**Key differences:**
- `@HiltViewModel` annotation
- `@Inject constructor` for dependency injection
- Hilt generates the factory automatically
- `hiltViewModel()` helper in Composable

**Advantage (Dagger):** Straightforward, less "magic", uses plain `ViewModel`  
**Advantage (Hilt):** `@Inject constructor` is cleaner, factory is auto-generated

---

## Lines of Code Comparison

| File | Dagger (PR #17) | Hilt (Workspace) | Difference |
|------|---|---|---|
| `MovieApplication.kt` | ~8 lines | ~2 lines | Hilt saves ~6 |
| `di/AppComponent.kt` | ~13 lines | **deleted** | Hilt saves ~13 |
| `di/AppModule.kt` | ~30 lines | ~30 lines | Same |
| `presentation/MainActivity.kt` | Plain class (~5 extra lines) | `@AndroidEntryPoint` (~1 line) | Hilt saves ~4 |
| `DashboardViewModel.kt` | Manual access (~3 lines) | `@Inject constructor` (~1 line) | Hilt saves ~2 |
| **Total DI boilerplate** | **~66 lines** | **~33 lines** | **Hilt saves ~33 (50% less)** |

**Biggest wins in Hilt:**
- Eliminates `AppComponent.kt` entirely
- Auto-generates `@AndroidEntryPoint` injection
- Auto-generates `@HiltViewModel` factory
- No manual component.inject(this) calls

**Biggest wins in Dagger:**
- Full explicit control
- No generated code "magic"
- Easy to debug (everything is written by you)
- Better for multi-platform projects

---

## Architecture Comparison Table

| Aspect | Dagger (PR #17) | Hilt (Workspace) |
|--------|---|---|
| **Framework** | Vanilla Dagger 2 | Dagger 2 + Android layer |
| **Component Definition** | `@Component` interface → `DaggerAppComponent` | `@InstallIn(Scope)` on modules |
| **Context Provision** | Manual Context parameter | `@ApplicationContext` built-in |
| **Activity Injection** | Plain Activity | `@AndroidEntryPoint` |
| **ViewModel Injection** | Manual: `(app as MovieApplication).appComponent.getUseCase()` | `@HiltViewModel` + `hiltViewModel()` |
| **Factory Generation** | Manual or framework | Automatic by Hilt |
| **Lifecycle Awareness** | Manual | Built-in (Activity, Fragment, Service scopes) |
| **Build-time Safety** | ✓ Graph validation | ✓ Graph validation |
| **Boilerplate** | More code | Less code |
| **Learning Curve** | Steeper | Easier (conventions) |
| **Best For** | Complex DI, multi-platform | Android apps, rapid development |
| **First Released** | 2015 | 2020 |

---

## When to Choose Which?

### Choose Dagger If:
- Building a multi-platform project (Android + Desktop)
- You want explicit control over every dependency
- Working with legacy code
- Need complete visibility into the DI graph
- Learning DI concepts (more educational)

### Choose Hilt If:
- Building an Android app only
- Want to move fast (less boilerplate)
- Prefer conventions over explicit configuration
- Team is familiar with Android architecture patterns
- Want built-in Android lifecycle scopes (Activity, Fragment, ViewModel)
