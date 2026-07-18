# Hilt Build Time & Runtime Flow

A complete breakdown of what happens when your app builds and runs with Hilt dependency injection.

---

## BUILD TIME

### What Hilt Does at Compile Time

When you run `./gradlew build`, here's what happens:

```
┌─────────────────────────────────────────────────────────────────┐
│  1. GRADLE STARTS BUILD                                         │
│     └─ Triggers KSP (Kotlin Symbol Processing)                  │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  2. KSP SCANS ANNOTATIONS                                       │
│     └─ Reads @HiltAndroidApp from MovieApplication              │
│     └─ Reads @Module + @Provides from AppModule                 │
│     └─ Reads @HiltViewModel + @Inject from DashboardViewModel   │
│     └─ Reads @AndroidEntryPoint from MainActivity               │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  3. HILT GENERATES CODE                                         │
│     └─ Component definitions (SingletonComponent wiring)         │
│     └─ Factories for each @Provides method                      │
│        ├─ provideMovieRemoteDataSource()                        │
│        ├─ provideMovieLocalDataSource()                         │
│        ├─ provideMovieRepository()                              │
│        └─ provideGetPopularMoviesUseCase()                      │
│     └─ ViewModel provider factories                              │
│     └─ Entry point glue code for @AndroidEntryPoint             │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  4. HILT VALIDATES DEPENDENCY GRAPH                             │
│     └─ Can DashboardViewModel be created?                       │
│     └─ Does it need GetPopularMoviesUseCase?                    │
│     └─ Is GetPopularMoviesUseCase provided? YES ✓               │
│     └─ Does GetPopularMoviesUseCase need MovieRepository?       │
│     └─ Is MovieRepository provided? YES ✓                       │
│     └─ ... and so on for entire chain                           │
│                                                                  │
│  IF ANY PROVIDER IS MISSING → BUILD FAILS ✗                    │
│  (You get a compile error, not a runtime crash!)                │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  5. KOTLIN/JAVA COMPILER COMPILES                               │
│     └─ Your source code                                         │
│     └─ Hilt-generated code (factories, components, wiring)      │
│     └─ All combined into .class files                           │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  6. APK IS PACKAGED                                             │
│     └─ Contains your code + all generated Hilt code             │
└─────────────────────────────────────────────────────────────────┘

```

### Generated Artifacts (in `build/generated/`)

After build completes, Hilt generates:
- **Component classes** — wiring definitions
- **Factory classes** — code that calls your `@Provides` methods
- **Module adapters** — binds modules to components
- These are hidden but compiled into your APK

---

## RUNTIME

### What Happens When App Launches and Runs

#### Phase 1: App Startup

```
┌─────────────────────────────────────────────────────────────────┐
│  1. ANDROID LAUNCHES APP                                        │
│     └─ Reads AndroidManifest.xml                                │
│     └─ Sees android:name=".MovieApplication"                   │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  2. ANDROID CREATES MOVIEAPPLICATION                            │
│     └─ Your existing MovieApplication class is instantiated    │
│     └─ (Hilt DID NOT generate this — you wrote it)             │
│                                                                  │
│  Code:                                                          │
│  @HiltAndroidApp                                                │
│  class MovieApplication : Application()                         │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  3. HILT INITIALIZES SINGLETON COMPONENT                        │
│     └─ Hilt's generated code sets up the app-level container    │
│     └─ Dependency graph is NOW READY (but objects not created   │
│        until requested)                                         │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  4. ANDROID CREATES MAINACTIVITY                                │
│     └─ Your existing MainActivity is instantiated               │
│                                                                  │
│  Code:                                                          │
│  @AndroidEntryPoint                                             │
│  class MainActivity : ComponentActivity()                       │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  5. HILT CONNECTS ACTIVITY TO COMPONENT                         │
│     └─ Hilt's generated entry point glue wires MainActivity     │
│        to the activity component (sub-component of the graph)   │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  6. MAINACTIVITY.ONCREATE() RUNS                                │
│     └─ setContent { DashboardScreen() }                         │
│     └─ DashboardScreen Composable is invoked                    │
└─────────────────────────────────────────────────────────────────┘
```

#### Phase 2: ViewModel Injection (The Key Moment!)

```
┌─────────────────────────────────────────────────────────────────┐
│  7. DASHBOARDSCREEN RUNS                                        │
│     └─ val viewModel: DashboardViewModel = hiltViewModel()      │
│                                                                  │
│  🚨 THIS TRIGGERS THE ENTIRE DEPENDENCY CHAIN                   │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  8. HILT LOOKS UP DASHBOARDVIEWMODEL                            │
│     └─ Hilt asks: "Do I have a binding for DashboardViewModel?"│
│     └─ YES ✓ — it's marked @HiltViewModel                      │
│     └─ Its constructor needs: GetPopularMoviesUseCase           │
│        → Hilt now must find/create this                         │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  9. HILT LOOKS UP GETPOPULARMOVIEUSECASE                        │
│     └─ Hilt asks: "Do I have a binding for this?"               │
│     └─ YES ✓ — it's in AppModule:                               │
│        @Provides                                                │
│        fun provideGetPopularMoviesUseCase(                      │
│            movieRepository: MovieRepository                     │
│        ): GetPopularMoviesUseCase                               │
│     └─ Its parameter needs: MovieRepository                     │
│        → Hilt now must find/create this                         │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  10. HILT LOOKS UP MOVIEREPOSITORY                              │
│      └─ Hilt checks AppModule for a @Provides method            │
│      └─ YES ✓ — it's provided:                                  │
│         @Provides                                               │
│         fun provideMovieRepository(                             │
│             remoteDataSource: MovieRemoteDataSource,            │
│             localDataSource: MovieLocalDataSource               │
│         ): MovieRepository                                      │
│      └─ It needs TWO things:                                    │
│         • MovieRemoteDataSource                                 │
│         • MovieLocalDataSource                                  │
│         → Hilt now must find/create both                        │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  11. HILT LOOKS UP MOVIEREMOTEDATASOURCE                        │
│      └─ Hilt checks AppModule                                   │
│      └─ YES ✓ — it's provided:                                  │
│         @Provides                                               │
│         fun provideMovieRemoteDataSource():                     │
│             MovieRemoteDataSource {                             │
│             return MovieRemoteDataSource()  ← NO PARAMETERS     │
│         }                                                       │
│      └─ NO DEPENDENCIES — Hilt creates it immediately!         │
│      └─ ✓ MovieRemoteDataSource CREATED                        │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  12. HILT LOOKS UP MOVIELOCALDATASOURCE                         │
│      └─ Hilt checks AppModule                                   │
│      └─ YES ✓ — it's provided:                                  │
│         @Provides                                               │
│         fun provideMovieLocalDataSource(                        │
│             @ApplicationContext context: Context               │
│         ): MovieLocalDataSource                                 │
│      └─ It needs: Context with @ApplicationContext qualifier    │
│      └─ HILT HAS THIS! — It's a built-in Android resource      │
│      └─ ✓ MovieLocalDataSource CREATED                         │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  13. HILT CREATES MOVIEREPOSITORY                               │
│      └─ Now both dependencies are ready:                        │
│         ✓ MovieRemoteDataSource (from Step 11)                  │
│         ✓ MovieLocalDataSource (from Step 12)                   │
│      └─ Calls: provideMovieRepository(remoteDS, localDS)        │
│      └─ ✓ MovieRepository CREATED                              │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  14. HILT CREATES GETPOPULARMOVIEUSECASE                        │
│      └─ Now MovieRepository is ready (from Step 13)             │
│      └─ Calls: provideGetPopularMoviesUseCase(repo)             │
│      └─ ✓ GetPopularMoviesUseCase CREATED                       │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  15. HILT CREATES DASHBOARDVIEWMODEL                            │
│      └─ Now GetPopularMoviesUseCase is ready (from Step 14)     │
│      └─ Calls: DashboardViewModel(@Inject constructor,          │
│             getPopularMoviesUseCase)                            │
│      └─ ✓ DashboardViewModel CREATED                           │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  16. HILT RETURNS DASHBOARDVIEWMODEL TO SCREEN                  │
│      └─ The entire dependency chain is now built!               │
│      └─ DashboardScreen receives the fully-wired ViewModel      │
│      └─ Screen can now observe state and render                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Dependency Chain — Visual Summary

### The Full Chain (as it's built)

```
        ┌─── Built last ────┐
        │                   ▼
    ┏━━━━━━━━━━━━━┓
    ┃ DashboardVM ┃  ← Needed by DashboardScreen
    ┗━━━━━━━┬━━━━┛
            │  needs
            ▼
    ┏━━━━━━━━━━━━━━━━━━━┓
    ┃ GetPopularMovies  ┃  ← @Provides by provideGetPopularMoviesUseCase()
    ┃     UseCase       ┃
    ┗━━━━━━━┬━━━━━━━━━┛
            │  needs
            ▼
    ┏━━━━━━━━━━━━━┓
    ┃ MovieRepo   ┃  ← @Provides by provideMovieRepository()
    ┗━━━┬─────┬──┛
        │ needs 2 things:
        ├────────────────┬────────────────┐
        ▼                ▼                ▼
    ┏━━━━━━━━━━━━┓  ┏━━━━━━━━━━━━┓
    ┃ RemoteDS   ┃  ┃ LocalDS    ┃  ← Both @Provides in AppModule
    ┣━━━━━━━━━━━━╋  ┣━━━━━━━━━━━━╋
    ┃ NO DEPS    ┃  ┃ NEEDS CTX  ┃
    ┃ ✓CREATED   ┃  ┃ (built-in) ┃
    ┃ (Step 11)  ┃  ┃ ✓CREATED   ┃
    ┗━━━━━━━━━━━━┛  ┃ (Step 12)  ┃
                    ┗━━━━━━━━━━━━┛
                    
        └─── Built first ───┘
```

---

## Key Insights

### 1. Build Time vs Runtime

| Aspect | When |
|--------|------|
| **Annotation scanning** | Build time |
| **Code generation** | Build time |
| **Graph validation** | Build time |
| **Object creation** | Runtime (when requested) |
| **Lazy initialization** | Objects only created when needed |

### 2. Without `@Singleton` or other scopes
- Each time `hiltViewModel()` is called, a NEW instance is created (lazy)
- For activities/viewmodels: managed by their own lifecycle scope
- For `@Singleton`: one instance per app lifetime

### 3. Build Failures = Safety
- Missing a `@Provides` method → **compilation fails**
- Typo in dependency name → **compilation fails**
- Circular dependencies → **compilation fails**
- You'll NEVER get a runtime "dependency not found" crash

### 4. @Singleton in SingletonComponent
- `AppModule` is installed in `SingletonComponent`
- All `@Provides` methods are `@Singleton` (single instance for app lifetime)
- Shared across all Activities/ViewModels

---

## Actual Code References

### What You Wrote
```kotlin
// MovieApplication.kt
@HiltAndroidApp
class MovieApplication : Application()  // ← You wrote this
```

```kotlin
// di/AppModule.kt
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

```kotlin
// DashboardViewModel.kt
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase  // ← Hilt injects this
) : ViewModel()
```

```kotlin
// DashboardScreen.kt
@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()  // ← Triggers entire chain
    ...
}
```

### What Hilt Generated
- Hilt component classes (invisible, in generated folders)
- Factory classes that call your `@Provides` methods
- Entry point wiring for `MainActivity`
- ViewModel factory for `DashboardViewModel`

---

## Summary

**Build Time:** Hilt scans code → generates factories & validation → compiles everything together.

**Runtime:** App starts → Hilt initializes graph → When screen requests ViewModel → Hilt walks the dependency chain → creates objects bottom-up → returns fully-wired ViewModel to screen.

The magic: **All wiring is pre-validated at build time**, so you get zero runtime surprises.
