package com.app.examenmovile.data.repository

import android.util.Log
import com.app.examenmovile.data.local.preferences.SudokuPreferences
import com.app.examenmovile.data.mapper.toDomain
import com.app.examenmovile.data.remote.api.SudokuApi
import com.app.examenmovile.domain.model.Solution
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.model.UserBoard
import com.app.examenmovile.domain.repository.SudokuRepository
import com.google.gson.Gson
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
        ): Sudoku? {
            preferences.getSudokuCache()?.let { cache ->
                if (cache.sudoku!!.size == size && cache.sudoku.difficulty == difficulty) {
                    return cache.sudoku
                }
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
                val sudoku = response.toDomain(size, difficulty)
                Log.d("size and difficulty antes de api", "$size and $difficulty")
                Log.d("ResultSudokuImpl", response.toString())

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

        override suspend fun refreshSudoku(
            size: Int,
            difficulty: String,
        ): Sudoku? {
            Log.d("size2", size.toString())
            preferences.clearCache()
            return try {
                val apiKey = "wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf"
                val response =
                    api.getSudoku(
                        apiKey = apiKey,
                        width = size,
                        height = size,
                        difficulty = difficulty,
                    )
                val sudoku = response.toDomain(size, difficulty)
                Log.d("Hola", sudoku.puzzle.toString())

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

        override suspend fun getSolution(
            size: Int,
            difficulty: String,
            puzzle: List<List<Int?>>,
        ): Solution {
            // Intentar obtener del caché primero
            preferences.getSolutionCache()?.let { cache ->
                if (cache.solution.size == size && cache.solution.difficulty == difficulty) {
                    return cache.solution
                }
            }

            return try {
                val filledPuzzle = puzzle.map { row -> row.map { it ?: 0 } }
                val puzzleJson = Gson().toJson(filledPuzzle)
                Log.d("PuzzleSent", puzzleJson)
                val apiKey = "wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf"
                val response =
                    api.getSolution(
                        apiKey = apiKey,
                        width = size,
                        height = size,
                        puzzle = puzzleJson,
                    )
                val solution = response.toDomain(size, difficulty)
                Log.d("RawResponse", response.toString())
                Log.d("Hola", solution.toString())

                solution
            } catch (e: Exception) {
                // Si hay error, intentar usar caché
                preferences.getSolutionCache()?.let { cache ->
                    return cache.solution
                } ?: throw e
            }
        }

        override suspend fun saveUserBoard(
            size: Int,
            difficulty: String,
            board: List<List<Int?>>,
        ) {
            preferences.saveUserBoard(UserBoard(size, difficulty, board))
        }

        override suspend fun isUserBoard(
            size: Int,
            difficulty: String,
        ): Boolean {
            val userBoard = preferences.getUserBoard()
            if (userBoard?.size == size && userBoard.difficulty == difficulty) {
                return true
            }
            return false
        }

        override suspend fun getUserBoard(): UserBoard? = preferences.getUserBoard()
    }
