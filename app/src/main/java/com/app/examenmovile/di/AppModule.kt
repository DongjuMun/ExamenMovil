package com.app.examenmovile.di

import android.content.Context
import com.app.examenmovile.data.local.preferences.SudokuPreferences
import com.app.examenmovile.data.remote.api.SudokuApi
import com.app.examenmovile.data.repository.SudokuRepositoryImpl
import com.app.examenmovile.domain.repository.SudokuRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://api.api-ninjas.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideSudokuApi(retrofit: Retrofit): SudokuApi = retrofit.create(SudokuApi::class.java)

    @Provides
    @Singleton
    fun provideSudokuPreferences(
        @ApplicationContext context: Context,
        gson: Gson,
    ): SudokuPreferences = SudokuPreferences(context, gson)

    @Provides
    @Singleton
    fun provideSudokuRepository(
        api: SudokuApi,
        preferences: SudokuPreferences,
    ): SudokuRepository = SudokuRepositoryImpl(api, preferences)
}
