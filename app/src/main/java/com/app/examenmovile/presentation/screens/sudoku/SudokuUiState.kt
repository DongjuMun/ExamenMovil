package com.app.examenmovile.presentation.screens.sudoku

import com.app.examenmovile.domain.model.Solution
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.model.UserBoard

data class SudokuUiState(
    val sudoku: Sudoku = Sudoku(0, "", emptyList()),
    val solution: Solution = Solution(0, "", "", emptyList()),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCorrect: Boolean? = null,
    val isUserBoard: Boolean = false,
    val userBoard: UserBoard? = null, // <-- null by default
)
