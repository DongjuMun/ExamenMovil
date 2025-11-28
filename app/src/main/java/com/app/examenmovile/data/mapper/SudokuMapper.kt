package com.app.examenmovile.data.mapper

import com.app.examenmovile.data.remote.dto.SudokuDto
import com.app.examenmovile.domain.model.Sudoku

fun SudokuDto.toDomain(
    size: Int,
    difficulty: String,
) = Sudoku(
    size = size,
    difficulty = difficulty,
    puzzle = puzzle,
)
