package com.app.examenmovile.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.app.examenmovile.data.local.model.SolutionCache
import com.app.examenmovile.data.local.model.SudokuCache
import com.app.examenmovile.data.local.model.UserCache
import com.app.examenmovile.data.local.preferences.PreferencesConstants.KEY_USER_BOARD
import com.app.examenmovile.domain.model.Solution
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.model.UserBoard
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuPreferences
    @Inject
    constructor(
        @ApplicationContext context: Context,
        private val gson: Gson,
    ) {
        private val prefs: SharedPreferences =
            context.getSharedPreferences(
                PreferencesConstants.PREF_NAME,
                Context.MODE_PRIVATE,
            )

        fun saveSudoku(sudoku: Sudoku) {
            prefs
                .edit()
                .putString(PreferencesConstants.KEY_SUDOKU_PUZZLE, gson.toJson(sudoku))
                .apply()
        }

        fun saveSolution(solution: Solution) {
            prefs
                .edit()
                .putString(PreferencesConstants.KEY_SUDOKU_SOLUTION, gson.toJson(solution))
                .apply()
        }

        fun getSudokuCache(): SudokuCache? {
            val puzzleJson = prefs.getString(PreferencesConstants.KEY_SUDOKU_PUZZLE, null)

            if (puzzleJson == null) return null

            val puzzleType = object : TypeToken<Sudoku>() {}.type

            val sudoku: Sudoku = gson.fromJson(puzzleJson, puzzleType)

            return SudokuCache(
                sudoku = sudoku,
            )
        }

        fun getSolutionCache(): SolutionCache? {
            val solutionJson = prefs.getString(PreferencesConstants.KEY_SUDOKU_SOLUTION, null)

            if (solutionJson == null) return null

            val solutionType = object : TypeToken<Solution>() {}.type

            val solution: Solution = gson.fromJson(solutionJson, solutionType)

            return SolutionCache(
                solution = solution,
            )
        }

        fun clearCache() {
            prefs.edit().clear().apply()
        }

        fun saveUserBoard(userBoard: UserBoard) {
            prefs
                .edit()
                .putString(PreferencesConstants.KEY_USER_BOARD, gson.toJson(userBoard))
                .apply()
        }

        fun getUserBoard(): UserBoard? {
            val userJson = prefs.getString(PreferencesConstants.KEY_USER_BOARD, null)
            if (userJson == null) return null

            val userType = object : TypeToken<UserBoard>() {}.type

            val user: UserBoard = gson.fromJson(userJson, userType)

            return user
        }
    }
