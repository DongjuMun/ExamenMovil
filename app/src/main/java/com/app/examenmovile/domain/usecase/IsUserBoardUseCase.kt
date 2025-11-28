package com.app.examenmovile.domain.usecase

import com.app.examenmovile.domain.model.UserBoard
import com.app.examenmovile.domain.repository.SudokuRepository
import javax.inject.Inject

class IsUserBoardUseCase
    @Inject
    constructor(
        private val repository: SudokuRepository,
    ) {
        suspend operator fun invoke(
            size: Int,
            difficulty: String,
        ): Boolean = repository.isUserBoard(size, difficulty)
    }
