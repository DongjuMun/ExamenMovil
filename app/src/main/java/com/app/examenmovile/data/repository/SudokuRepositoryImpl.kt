package com.app.examenmovile.data.repository

import com.app.examenmovile.data.local.preferences.SudokuPreferences
import com.app.examenmovile.data.mapper.toDomain
import com.app.examenmovile.data.remote.api.SudokuApi
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.repository.SudokuRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepositoryImpl
    @Inject
    constructor(
        private val api: SudokuApi,
        private val preferences: SudokuPreferences,
    ) : SudokuRepository {
        override suspend fun getSudoku(
            size: Int,
            difficulty: String,
        ): Sudoku {
            // Intentar obtener del caché primero
            preferences.getSudokuCache()?.let { cache ->
                return cache.sudoku
            }

            return try {
                val apiKey = "wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf"
                val response =
                    api.getSudoku(
                        apiKey = apiKey,
                        width = size,
                        height = size,
                        difficulty = difficulty,
                    )
                val sudoku = response.toDomain()

                // Guardar en caché
                preferences.saveSudoku(
                    sudoku = sudoku,
                )

                sudoku
            } catch (e: Exception) {
                // Si hay error, intentar usar caché
                preferences.getSudokuCache()?.let { cache ->
                    return cache.sudoku
                } ?: throw e
            }
        }
    }
