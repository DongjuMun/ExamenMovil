@file:Suppress("ktlint:standard:import-ordering")

package com.app.examenmovile.presentation.screens.sudoku

import com.app.examenmovile.presentation.screens.sudoku.SudokuUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examenmovile.domain.common.Result
import com.app.examenmovile.domain.usecase.GetSudokuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SudokuViewModel
    @Inject
    constructor(
        private val getSudokuUseCase: GetSudokuUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SudokuUiState())
        val uiState: StateFlow<SudokuUiState> = _uiState.asStateFlow()

        public fun getSudoku(
            size: Int,
            difficulty: String,
        ) {
            viewModelScope.launch {
                getSudokuUseCase(size, difficulty).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success ->
                                state.copy(
                                    sudoku = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            is Result.Error ->
                                state.copy(
                                    error = result.exception.message,
                                    isLoading = false,
                                )
                        }
                    }
                }
            }
        }
    }
