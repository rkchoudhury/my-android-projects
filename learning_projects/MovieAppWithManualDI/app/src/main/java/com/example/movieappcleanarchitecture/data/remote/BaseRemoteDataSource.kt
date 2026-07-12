package com.example.movieappcleanarchitecture.data.remote

import com.example.movieappcleanarchitecture.common.API_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Base class for all remote data sources.
 *
 * WHY THIS CLASS EXISTS:
 * - Retrofit setup (base URL, Gson converter) is the same for every API service.
 * - Without this, every data source would duplicate the Retrofit.Builder() code.
 * - New data sources just extend this class and call createService().
 *
 * WHY companion object?
 * - "companion object" means there's only ONE Retrofit instance shared across
 *   ALL data sources. Without it, each MovieRemoteDataSource() or
 *   ActorRemoteDataSource() would create its own Retrofit — wasteful.
 * - Think of companion object as a "static" shared space in Java.
 *
 * WHY "by lazy"?
 * - The Retrofit instance is created only when first accessed, not at app startup.
 * - After that, the same instance is reused every time.
 *
 * HOW TO USE:
 *   class MovieRemoteDataSource : BaseRemoteDataSource() {
 *       private val movieApiService = createService(MovieApiService::class.java)
 *   }
 *
 *   class ActorRemoteDataSource : BaseRemoteDataSource() {
 *       private val actorApiService = createService(ActorApiService::class.java)
 *   }
 */
abstract class BaseRemoteDataSource {

    companion object {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    /**
     * Creates a Retrofit service for the given interface.
     *
     * Example: createService(MovieApiService::class.java) → returns MovieApiService instance
     */
    protected fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
