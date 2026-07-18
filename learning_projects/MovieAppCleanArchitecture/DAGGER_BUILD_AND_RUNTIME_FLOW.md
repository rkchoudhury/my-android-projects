# Dagger Build Time & Runtime Flow

A complete breakdown of what happens when your app builds and runs with **Dagger** dependency injection (not Hilt).

---

## BUILD TIME

### What Dagger Does at Compile Time

When you run `./gradlew build`:

```
┌─────────────────────────────────────────────────────────────────┐
│  1. GRADLE STARTS BUILD                                         │
│     └─ Triggers KSP (Kotlin Symbol Processing)                  │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  2. KSP SCANS ANNOTATIONS                                       │
│     └─ Reads @Component from AppComponent interface              │
│     └─ Reads @Module from AppModule class                       │
│     └─ Reads @Provides methods in AppModule                     │
│     └─ Reads @BindsInstance in AppComponent.Factory             │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  3. DAGGER GENERATES CODE                                       │
│     └─ DaggerAppComponent (implements AppComponent interface)    │
│     └─ Module adapters for each @Provides method                │
│        ├─ Adapter for provideMovieRemoteDataSource()            │
│        ├─ Adapter for provideMovieLocalDataSource()             │
│        ├─ Adapter for provideMovieRepository()                  │
│        └─ Adapter for provideGetPopularMoviesUseCase()          │
│     └─ Factory wrappers to call these adapters                  │
│     └─ Component initialization logic                            │
└─────────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────────┐
│  4. DAGGER VALIDATES DEPENDENCY GRAPH                           │
│     └─ Does AppComponent expose getPopularMoviesUseCase()?      │
│     └─ Is there a @Provides for GetPopularMoviesUseCase? YES ✓  │
│     └─ Does it need MovieRepository?                           │
│     └─ Is there a @Provides for MovieRepository? YES ✓          │
│     └─ ... and so on for entire chain                           │
│                                                                  │
│  IF ANY PROVIDER IS MISSING → BUILD FAILS ✗                    │
│  (You get a compile error, not a runtime crash!)                │
└─────────────────────────────────────────────────────────────────┘
```

---

## RUNTIME

### Dagger Initializes at App Launch

When your app starts, here's the initialization sequence:

```
┌──────────────────────────────────────────────────────────────────┐
│  STEP 1: APP LAUNCH                                              │
│          - Android creates the Application instance              │
│          - Calls MovieApplication.onCreate()                     │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 2: CREATE DAGGER COMPONENT                                 │
│          - In MovieApplication.onCreate()                        │
│          - Calls: DaggerAppComponent.factory().create(this)      │
│          - DaggerAppComponent is auto-generated at build time    │
│          - Stores appComponent as a public property              │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 3: ANDROID CREATES MAIN ACTIVITY                           │
│          - MainActivity is instantiated                          │
│          - MainActivity.onCreate() runs                          │
│          - Calls: setContent { DashboardScreen() }               │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 4: COMPOSABLE CREATES VIEWMODEL                            │
│          - DashboardScreen() composable renders                  │
│          - Calls: DashboardViewModel(application)                │
│          - Passes the Application instance                       │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 5: VIEWMODEL ACCESSES DAGGER COMPONENT                     │
│          - In DashboardViewModel init block:                     │
│          - (application as MovieApplication).appComponent        │
│          - Calls: .getPopularMoviesUseCase()                     │
│          - THIS TRIGGERS DAGGER TO BUILD THE ENTIRE CHAIN!       │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 6: DAGGER RESOLVES DEPENDENCY CHAIN                        │
│          - DaggerAppComponent.getPopularMoviesUseCase()          │
│          - Checks AppModule.provideGetPopularMoviesUseCase()     │
│          - Needs: MovieRepository (parameter)                    │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 7: LOOKUP MovieRepository PROVIDER                         │
│          - Checks AppModule.provideMovieRepository(...)          │
│          - Needs: MovieRemoteDataSource + MovieLocalDataSource   │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 8: LOOKUP MovieRemoteDataSource PROVIDER                   │
│          - Checks AppModule.provideMovieRemoteDataSource()       │
│          - No dependencies needed                                │
│          - Creates new MovieRemoteDataSource() instance          │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 9: LOOKUP MovieLocalDataSource PROVIDER                    │
│          - Checks AppModule.provideMovieLocalDataSource(ctx)     │
│          - Needs: Context (method parameter)                     │
│          - Uses the Context from @BindsInstance in factory       │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 10: CREATE ALL INSTANCES (BOTTOM-UP)                       │
│           - MovieRemoteDataSource() created                      │
│           - MovieLocalDataSource(context) created with Context   │
│           - MovieRepositoryImpl(...) created with both DSs        │
│           - GetPopularMoviesUseCase(...) created with Repository │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 11: RETURN GetPopularMoviesUseCase                          │
│           - Fully-constructed use case returned to ViewModel     │
│           - ViewModel stores it in: getPopularMovies field       │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 12: VIEWMODEL.INIT() RUNS                                  │
│           - ViewModel is fully constructed                       │
│           - init { fetchMovies() } is called                     │
│           - Coroutine is launched in viewModelScope              │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 13: CALL getPopularMovies() USECASE                        │
│           - getPopularMovies() is invoked                        │
│           - Calls repository.getPopularMovies()                  │
│           - Coroutine fetches from API                           │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 14: RESPONSE MAPPED & STATE UPDATED                        │
│           - API JSON → MovieApiModel → Movie objects             │
│           - _uiState.value = DashboardUiState.Success(movies)    │
│           - Recomposition triggered                              │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 15: SCREEN RECOMPOSES WITH NEW STATE                       │
│           - DashboardScreen observes uiState                     │
│           - For each movie in state: render MovieCard            │
│           - Poster image loaded via Coil                         │
└──────────────────────┬───────────────────────────────────────────┘
                       ↓
┌──────────────────────────────────────────────────────────────────┐
│  STEP 16: USER SEES MOVIES                                       │
│           - Popular movies displayed with poster images          │
│           - Full dependency chain complete                       │
│           - App ready for user interaction                       │
└──────────────────────────────────────────────────────────────────┘
```

---

## Key Points

**When objects are created:**
- Step 2: Component instance created (movieApplication.appComponent)
- Steps 5-10: All dependencies created when `getPopularMoviesUseCase()` is called

**Manual vs. Dagger:**
- **Manual:** ViewModel would manually access component and request the use case
- **Dagger:** Component intercepts the request and builds the entire chain

**Dependency Graph Chain (BOTTOM-UP):**
```
Context ──→ MovieLocalDataSource ──┐
├──→ MovieRepositoryImpl ──→ GetPopularMoviesUseCase
MovieRemoteDataSource ─────────────┘
```

---

## Dagger vs. Hilt Comparison

| Aspect | Dagger | Hilt |
|--------|--------|------|
| **Component** | Manual `@Component` interface | Android-aware components (SingletonComponent, ActivityComponent, etc.) |
| **Initialization** | Manual `DaggerAppComponent.factory().create(context)` in Application | Auto-initialized by Hilt at app startup |
| **ViewModel Injection** | Manual component access: `(app as MovieApplication).appComponent.getUseCase()` | `@HiltViewModel` + `@Inject constructor` |
| **View Model Access** | Compose: Manual pass-through or service locator | `hiltViewModel<ViewModel>()` in Compose |
| **Scope Management** | Define custom scopes per need | Pre-defined scopes (SingletonComponent, ActivityComponent, etc.) |
| **Android Integration** | No built-in Android awareness | Seamless Android lifecycle integration |
| **Boilerplate** | More code needed (Component interfaces, factory patterns) | Minimal boilerplate with annotations |
| **Compile-time Safety** | ✓ Yes - graph validation at build | ✓ Yes - graph validation at build |
| **Learning Curve** | Steeper - explicit about everything | Easier - conventions over configuration |
| **Best For** | Pure Java/Kotlin, complex DI requirements | Android apps with lifecycle needs |

---

## When Each was Created

**Dagger 2:** 2015 - Google's first compiler-based DI framework for Android  
**Hilt:** 2020 - Built on Dagger 2, simplified for Android-specific use cases

Hilt is basically **Dagger 2 + pre-configured Android components** to reduce boilerplate.
