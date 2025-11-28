package com.app.examenmovile.domain.usecase

import com.app.examenmovile.domain.common.Result
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.repository.SudokuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RefreshSudokuUsecase
    @Inject
    constructor(
        private val repository: SudokuRepository,
    ) {
        operator fun invoke(
            size: Int,
            difficulty: String,
        ): Flow<Result<Sudoku>> =
            flow {
                try {
                    emit(Result.Loading)
                    val sudoku = repository.refreshSudoku(size, difficulty)
                    emit(Result.Success(sudoku ?: Sudoku(0, "", emptyList())))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
