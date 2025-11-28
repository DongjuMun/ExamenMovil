package com.app.examenmovile.presentation.screens.sudoku

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examenmovile.domain.common.Result
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.domain.model.UserBoard
import com.app.examenmovile.domain.usecase.GetSolutionUseCase
import com.app.examenmovile.domain.usecase.GetSudokuUseCase
import com.app.examenmovile.domain.usecase.GetUserBoardUseCase
import com.app.examenmovile.domain.usecase.IsUserBoardUseCase
import com.app.examenmovile.domain.usecase.RefreshSudokuUsecase
import com.app.examenmovile.domain.usecase.SaveUserBoardUseCase
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
        private val getSolutionUseCase: GetSolutionUseCase,
        private val refreshSudokuUsecase: RefreshSudokuUsecase,
        private val saveUserBoardUseCase: SaveUserBoardUseCase,
        private val getUserBoardUseCase: GetUserBoardUseCase,
        private val isUserBoardUseCase: IsUserBoardUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SudokuUiState())
        val uiState: StateFlow<SudokuUiState> = _uiState.asStateFlow()

        fun getSudoku(
            size: Int,
            difficulty: String,
        ) {
            viewModelScope.launch {
                getSudokuUseCase(size, difficulty).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading -> state.copy(isLoading = true)
                            is Result.Success -> {
                                Log.d("VM", "Loaded sudoku: ${result.data}")
                                state.copy(sudoku = result.data, isLoading = false, error = null)
                            }
                            is Result.Error -> state.copy(error = result.exception.message, isLoading = false)
                        }
                    }
                }
            }
        }

        fun refreshSudoku(
            size: Int,
            difficulty: String,
        ) {
            viewModelScope.launch {
                // clear any saved userBoard for this puzzle before fetching a new puzzle
                clearUserBoard()
                refreshSudokuUsecase(size, difficulty).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading -> state.copy(isLoading = true)
                            is Result.Success -> {
                                Log.d("VM", "Refreshed sudoku: ${result.data}")
                                state.copy(sudoku = result.data, isLoading = false, error = null)
                            }
                            is Result.Error -> state.copy(error = result.exception.message, isLoading = false)
                        }
                    }
                }
            }
        }

        fun getSolution(
            size: Int,
            difficulty: String,
            puzzle: List<List<Int?>>,
        ) {
            viewModelScope.launch {
                getSolutionUseCase(size, difficulty, puzzle).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading -> state.copy(isLoading = true)
                            is Result.Success -> {
                                Log.d("VM", "Solution result: ${result.data}")
                                val solved = result.data
                                val isCorrect = solved.status == "solved"
                                state.copy(solution = solved, isCorrect = isCorrect, isLoading = false, error = null)
                            }
                            is Result.Error -> state.copy(error = result.exception.message, isLoading = false)
                        }
                    }
                }
            }
        }

        fun getSavedUserBoard() {
            viewModelScope.launch {
                val saved: UserBoard? = getUserBoardUseCase()
                Log.d("VM", "Loaded saved userBoard: $saved")
                _uiState.update { it.copy(userBoard = saved) }
            }
        }

        fun isUserBoard(
            size: Int,
            difficulty: String,
        ) {
            viewModelScope.launch {
                val correct: Boolean = isUserBoardUseCase(size, difficulty)
                Log.d("VM", "isUserBoard? $correct")
                _uiState.update { it.copy(isUserBoard = correct) }
            }
        }

        fun saveUserBoard(
            size: Int,
            difficulty: String,
            board: List<List<Int?>>,
        ) {
            viewModelScope.launch {
                saveUserBoardUseCase(size, difficulty, board)
                // update UI state so the screen can read the latest saved board if needed
                _uiState.update { it.copy(userBoard = UserBoard(size, difficulty, board), isUserBoard = true) }
                Log.d("VM", "Saved userBoard for size=$size difficulty=$difficulty")
            }
        }

        fun clearUserBoard() {
            viewModelScope.launch {
                // saving a "null" style empty board in preferences can be implementation-specific;
                // Our repository preferences layer should handle special case (size=0, empty) as "no data".
                saveUserBoardUseCase(0, "", emptyList())
                _uiState.update { it.copy(userBoard = null, isUserBoard = false) }
                Log.d("VM", "Cleared userBoard cache")
            }
        }

        fun setIsCorrect(correct: Boolean) {
            _uiState.update { it.copy(isCorrect = correct) }
        }

        fun reloadUserBoard(
            size: Int,
            difficulty: String,
            onLoaded: (UserBoard?) -> Unit,
        ) {
            viewModelScope.launch {
                val saved = getUserBoardUseCase()
                Log.d("Saved?", saved.toString())
                val exists = isUserBoardUseCase(size, difficulty)
                Log.d("IsUser?", exists.toString())

                if (!exists) {
                    onLoaded(null)
                    return@launch
                }

                _uiState.update { it.copy(userBoard = saved, isUserBoard = saved != null) }

                onLoaded(saved)
            }
        }
    }
