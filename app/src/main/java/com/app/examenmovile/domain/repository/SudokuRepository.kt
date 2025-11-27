package com.app.examenmovile.domain.repository

import com.app.examenmovile.domain.model.Sudoku

interface SudokuRepository {
    suspend fun getSudoku(
        size: Int,
        difficulty: String,
    ): Sudoku
}
