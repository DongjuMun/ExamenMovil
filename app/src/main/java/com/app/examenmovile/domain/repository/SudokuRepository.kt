package com.app.examenmovile.domain.repository

import com.app.examenmovile.domain.model.Solution
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.model.UserBoard

interface SudokuRepository {
    suspend fun getSudoku(
        size: Int,
        difficulty: String,
    ): Sudoku?

    suspend fun refreshSudoku(
        size: Int,
        difficulty: String,
    ): Sudoku?

    suspend fun getSolution(
        size: Int,
        difficulty: String,
        puzzle: List<List<Int?>>,
    ): Solution

    suspend fun saveUserBoard(
        size: Int,
        difficulty: String,
        board: List<List<Int?>>,
    )

    suspend fun isUserBoard(
        size: Int,
        difficulty: String,
    ): Boolean

    suspend fun getUserBoard(): UserBoard?
}
