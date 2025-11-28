package com.app.examenmovile.domain.model

data class Sudoku(
    val size: Int,
    val difficulty: String?,
    val puzzle: List<List<Int?>>,
)
