package com.app.examenmovile.presentation.screens.sudoku

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.examenmovile.domain.model.UserBoard
import com.app.examenmovile.presentation.screens.sudoku.components.SudokuCell
import com.app.examenmovile.presentation.screens.sudoku.components.SudokuNumberButton

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen(
    size: Int,
    difficulty: String,
    onBackClick: () -> Unit,
    viewModel: SudokuViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedRow by remember { mutableStateOf(-1) }
    var selectedCol by remember { mutableStateOf(-1) }
    var boardInitialized by remember { mutableStateOf(false) }
    var forceRefresh by remember { mutableStateOf(false) }

    val boardSize = size * size

    // rows are MutableList<Int?> so Compose will recompose on changes
    val board = remember { mutableStateListOf<MutableList<Int?>>() }
    val originalPuzzle = remember { mutableStateListOf<MutableList<Int?>>() }
    var shouldCheck by remember { mutableStateOf(false) }

    LaunchedEffect(size, difficulty) {
        viewModel.getSudoku(size, difficulty)
        viewModel.isUserBoard(size, difficulty)
    }

    LaunchedEffect(uiState.sudoku, uiState.userBoard) {
        if (boardInitialized && !forceRefresh) return@LaunchedEffect // <-- prevents overwriting board

        val puzzle = uiState.sudoku.puzzle
        if (puzzle.isEmpty()) return@LaunchedEffect

        originalPuzzle.clear()
        originalPuzzle.addAll(puzzle.map { it.toMutableList() })

        val saved = uiState.userBoard
        val shouldUseSaved =
            saved != null &&
                saved.size == boardSize &&
                saved.difficulty == difficulty &&
                saved.user.isNotEmpty()

        board.clear()

        if (forceRefresh || !shouldUseSaved) {
            board.addAll(puzzle.map { it.toMutableList() })
            Log.d("UI", "Using saved userBoard")
        } else {
            board.addAll(saved.user.map { it.toMutableList() })
            Log.d("UI", "Loaded puzzle into board")
        }

        boardInitialized = true
        forceRefresh = false
        viewModel.getSolution(size, difficulty, board.map { it.toList() })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sudoku $boardSize×$boardSize - $difficulty") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiState.isLoading) {
                Text("Cargando...", color = Color.Black)
                return@Column
            }

            if (uiState.error != null) {
                Text("Error: ${uiState.error}", color = Color.Red)
                return@Column
            }

            if (board.isEmpty()) {
                Text("Esperando tablero...", color = Color.Black)
                return@Column
            } else {
                // Sudoku board
                val gridSizeDp = 35.dp * boardSize
                LazyVerticalGrid(
                    columns = GridCells.Fixed(boardSize),
                    modifier =
                        Modifier
                            .size(gridSizeDp)
                            .background(Color.Black),
                ) {
                    items(boardSize * boardSize) { index ->
                        val row = index / boardSize
                        val col = index % boardSize
                        val value = board[row][col]

                        SudokuCell(
                            value = value,
                            isClue = originalPuzzle[row][col] != null,
                            isSelected = (row == selectedRow && col == selectedCol),
                            onClick = {
                                selectedRow = row
                                selectedCol = col
                                shouldCheck = false
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Number pad — when the user edits, we save the board immediately
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(color = Color.DarkGray)
                            .horizontalScroll(rememberScrollState()),
                ) {
                    val maxNumber = boardSize
                    for (n in 1..maxNumber) {
                        SudokuNumberButton(
                            number = n,
                            onClick = {
                                if (selectedRow != -1 && selectedCol != -1) {
                                    // prevent editing clues
                                    if (originalPuzzle[selectedRow][selectedCol] != null) {
                                        return@SudokuNumberButton
                                    }
                                    val newRow = board[selectedRow].toMutableList()
                                    newRow[selectedCol] = n
                                    board[selectedRow] = newRow

                                    // Save the user's current board
                                    viewModel.saveUserBoard(size, difficulty, board.map { it.toList() })
                                }
                                shouldCheck = false
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.reloadUserBoard(size, difficulty) { saved ->
                            if (saved != null) {
                                board.clear()
                                board.addAll(saved.user.map { it.toMutableList() })
                                Log.d("Sudoku", "Reloaded saved progress")
                            } else {
                                Log.d("Sudoku", "No saved progress to reload")
                            }
                        }
                    },
                ) {
                    Text("Reload Progress")
                }

                Button(onClick = {
                    if (board.isEmpty() || board.any { it.isEmpty() }) {
                        Log.e("Sudoku", "Board not ready, cannot check solution")
                        return@Button
                    }

                    uiState.solution.solution?.let { solutionGrid ->
                        val nonNullSolution = solutionGrid.map { row -> row.map { it ?: 0 } }
                        val nonNullBoard = board.map { row -> row.map { it ?: 0 } }

                        val correct = isBoardCorrect(nonNullBoard, nonNullSolution)
                        viewModel.setIsCorrect(correct)
                    } ?: Log.e("Sudoku", "Solution not loaded yet")

                    shouldCheck = true
                }) {
                    Text("Check Solution")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    // reset to original puzzle (not to saved userBoard)
                    board.clear()
                    board.addAll(originalPuzzle.map { it.toMutableList() })
                    viewModel.saveUserBoard(boardSize, difficulty, board.map { it.toList() })
                    selectedRow = -1
                    selectedCol = -1
                    shouldCheck = false
                }) {
                    Text("Empty Cells")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    // clear any saved user board and request a fresh puzzle
                    viewModel.clearUserBoard()
                    forceRefresh = true
                    boardInitialized = false
                    viewModel.refreshSudoku(size, difficulty)
                    shouldCheck = false
                }) {
                    Text("New Puzzle")
                }

                if (shouldCheck) {
                    uiState.isCorrect?.let { correct ->
                        Spacer(modifier = Modifier.height(12.dp))

                        if (correct) {
                            Text(
                                "✔ Sudoku solved correctly!",
                                color = Color(0xFF2E7D32),
                                fontSize = 18.sp,
                            )
                        } else {
                            Text(
                                "✖ Incorrect solution. Check your entries.",
                                color = Color.Red,
                                fontSize = 18.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

fun isBoardCorrect(
    board: List<List<Int?>>,
    solution: List<List<Int>>,
): Boolean {
    if (board.size != solution.size) return false

    for (i in board.indices) {
        if (board[i].size != solution[i].size) return false

        for (j in board[i].indices) {
            val cell = board[i][j] ?: return false
            if (cell != solution[i][j]) return false
        }
    }
    return true
}
