package com.app.examenmovile.presentation.screens.sudoku

import com.app.examenmovile.domain.model.Sudoku

data class SudokuUiState(
    val sudoku: Sudoku = Sudoku(emptyList()),
    val isLoading: Boolean = false,
    val error: String? = null,
)
