package com.app.examenmovile.domain.usecase

import com.app.examenmovile.domain.repository.SudokuRepository
import javax.inject.Inject

class SaveUserBoardUseCase
    @Inject
    constructor(
        private val repository: SudokuRepository,
    ) {
        suspend operator fun invoke(
            size: Int,
            difficulty: String,
            board: List<List<Int?>>,
        ) {
            repository.saveUserBoard(size, difficulty, board)
        }
    }
