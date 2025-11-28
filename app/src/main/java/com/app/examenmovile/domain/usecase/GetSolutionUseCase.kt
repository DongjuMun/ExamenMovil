package com.app.examenmovile.domain.usecase

import com.app.examenmovile.domain.common.Result
import com.app.examenmovile.domain.model.Solution
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.repository.SudokuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSolutionUseCase
    @Inject
    constructor(
        private val repository: SudokuRepository,
    ) {
        operator fun invoke(
            size: Int,
            difficulty: String,
            puzzle: List<List<Int?>>,
        ): Flow<Result<Solution>> =
            flow {
                try {
                    emit(Result.Loading)
                    val solution = repository.getSolution(size, difficulty, puzzle)
                    emit(Result.Success(solution))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
