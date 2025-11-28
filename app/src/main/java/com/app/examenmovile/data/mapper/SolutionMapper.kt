package com.app.examenmovile.data.mapper

import com.app.examenmovile.data.remote.dto.SolutionDto
import com.app.examenmovile.domain.model.Solution
import com.app.examenmovile.domain.model.Sudoku
import kotlin.Int

fun SolutionDto.toDomain(
    size: Int,
    difficulty: String,
) = Solution(
    size = size,
    difficulty = difficulty,
    status = status,
    solution = solution,
)
