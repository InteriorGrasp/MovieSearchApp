package com.georgian.moviesearchapp.di

import com.georgian.moviesearchapp.data.network.ApiService
import com.georgian.moviesearchapp.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiService.create() // Use the existing create method which initializes HttpClient
    }

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: ApiService): MovieRepository {
        return MovieRepository(apiService) // Provide the repository with the ApiService
    }
}