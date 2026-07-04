# Dagger vs Hilt — Before & After (Movie App)

A concrete code comparison showing what changed when migrating from pure Dagger to Hilt,
based on the actual files in this project.

---

## 1. Application Class

### Dagger — Before (~10 lines, manual component setup)

```kotlin
// MovieApplication.kt
class MovieApplication : Application() {

    // Step 1: Declare the component
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        // Step 2: Build it manually
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
```

### Hilt — After (~2 lines)

```kotlin
// MovieApplication.kt
@HiltAndroidApp
class MovieApplication : Application()
```

> Hilt auto-generates the component, initializes it, and ties it to the app lifecycle.

---

## 2. AppComponent Interface — Deleted Entirely

### Dagger — Before (entire file you'd have to write)

```kotlin
// AppComponent.kt — DOES NOT EXIST in this project (Hilt eliminated it)
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(viewModel: DashboardViewModel)
    // Every new Activity/ViewModel would need a new line here ↑
}
```

### Hilt — After

```
FILE DELETED — Hilt generates SingletonComponent automatically.
No AppComponent.kt exists in this project.
```

---

## 3. AppModule

### Dagger — Before (extra @Singleton on every provider, manual Context wiring)

```kotlin
@Module
object AppModule {

    // ❌ Need @Singleton manually on every provider
    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(): MovieRemoteDataSource = MovieRemoteDataSource()

    // ❌ Context injection requires @ApplicationContext qualifier manually wired into the component
    @Singleton
    @Provides
    fun provideMovieLocalDataSource(context: Context): MovieLocalDataSource =
        MovieLocalDataSource(context)

    @Singleton
    @Provides
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MovieRepository = MovieRepositoryImpl(remoteDataSource, localDataSource)

    @Singleton
    @Provides
    fun provideGetPopularMoviesUseCase(repo: MovieRepository): GetPopularMoviesUseCase =
        GetPopularMoviesUseCase(repo)
}
```

### Hilt — After (`di/AppModule.kt`)

```kotlin
@Module
@InstallIn(SingletonComponent::class)  // ← replaces the entire @Component wiring
object AppModule {

    @Provides
    fun provideMovieRemoteDataSource(): MovieRemoteDataSource = MovieRemoteDataSource()

    @Provides
    fun provideMovieLocalDataSource(
        @ApplicationContext context: Context  // ← built-in qualifier, no manual wiring
    ): MovieLocalDataSource = MovieLocalDataSource(context)

    @Provides
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MovieRepository = MovieRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    fun provideGetPopularMoviesUseCase(repo: MovieRepository): GetPopularMoviesUseCase =
        GetPopularMoviesUseCase(repo)
}
```

> `@InstallIn(SingletonComponent::class)` replaces the entire manual `@Component` + `@Subcomponent` chain.

---

## 4. MainActivity

### Dagger — Before (manual field injection + inject() call)

```kotlin
class MainActivity : ComponentActivity() {
    // ❌ Must declare every injected field with @Inject
    @Inject lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ❌ Must manually trigger injection before using any @Inject fields
        (application as MovieApplication).appComponent.inject(this)
        setContent { ... }
    }
}
```

### Hilt — After (`presentation/MainActivity.kt`)

```kotlin
@AndroidEntryPoint  // ← one annotation, injection is automatic
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DashboardScreen() }
    }
}
```

---

## 5. DashboardViewModel

### Dagger — Before (required a separate ViewModelFactory class)

```kotlin
// ❌ Had to write this entire factory class
class DashboardViewModelFactory(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(getPopularMovies) as T
    }
}

// ❌ ViewModel itself had no @Inject — factory handled construction
class DashboardViewModel(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() { ... }
```

And in MainActivity:

```kotlin
// ❌ Manually instantiate factory and pass it to viewModels()
val factory = DashboardViewModelFactory(appComponent.getPopularMoviesUseCase())
val viewModel by viewModels<DashboardViewModel> { factory }
```

### Hilt — After (`presentation/screens/dashboard/DashboardViewModel.kt`)

```kotlin
@HiltViewModel  // ← Hilt generates the factory automatically
class DashboardViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() { ... }
```

And in `DashboardScreen.kt`:

```kotlin
val viewModel: DashboardViewModel = hiltViewModel()  // ← just works
```

---

## Lines of Code Saved

| File | Dagger | Hilt (this project) | Saved |
|------|--------|----------------------|-------|
| `MovieApplication.kt` | ~10 lines | ~2 lines | ~8 |
| `AppComponent.kt` | ~8 lines | deleted | ~8 |
| `AppModule.kt` | ~35 lines | ~30 lines | ~5 |
| `MainActivity.kt` | ~5 extra lines | 0 extra | ~5 |
| `DashboardViewModelFactory.kt` | ~10 lines | deleted | ~10 |
| **Total** | **~68 lines** | **~32 lines** | **~36 lines (~53% less)** |

The biggest wins in this app:
- No `AppComponent.kt`
- No `ViewModelFactory`
- No manual `inject()` call in `MainActivity`

---

## Key Annotation Mapping

| Dagger | Hilt Equivalent | What changed |
|--------|-----------------|--------------|
| `@Component(modules = [...])` | `@InstallIn(SingletonComponent::class)` | No interface needed |
| Manual `DaggerAppComponent.builder().build()` | `@HiltAndroidApp` | Auto-generated |
| `appComponent.inject(this)` in Activity | `@AndroidEntryPoint` | No manual call |
| `ViewModelProvider.Factory` | `@HiltViewModel` | Factory generated |
| `@Singleton` per provider | Inherited from `SingletonComponent` | Less repetition |
| Manual Context parameter | `@ApplicationContext` qualifier | Built-in support |
