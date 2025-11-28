package com.app.examenmovile.domain.usecase

import com.app.examenmovile.domain.model.UserBoard
import com.app.examenmovile.domain.repository.SudokuRepository
import javax.inject.Inject

class GetUserBoardUseCase
    @Inject
    constructor(
        private val repository: SudokuRepository,
    ) {
        suspend operator fun invoke(): UserBoard? = repository.getUserBoard()
    }
