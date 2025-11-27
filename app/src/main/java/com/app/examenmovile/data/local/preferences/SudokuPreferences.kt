package com.app.examenmovile.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.app.examenmovile.data.local.model.SudokuCache
import com.app.examenmovile.domain.model.Sudoku
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
                .putString(PreferencesConstants.KEY_SUDOKU_CACHE, gson.toJson(sudoku))
                .apply()
        }

        fun getSudokuCache(): SudokuCache? {
            val json = prefs.getString(PreferencesConstants.KEY_SUDOKU_CACHE, null)

            if (json == null) return null

            val type = object : TypeToken<Sudoku>() {}.type
            val sudoku: Sudoku = gson.fromJson(json, type)

            return SudokuCache(
                sudoku = sudoku,
            )
        }
    }
