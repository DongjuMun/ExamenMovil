package com.app.examenmovile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.examenmovile.domain.model.Sudoku
import com.app.examenmovile.presentation.screens.home.HomeScreen
import com.app.examenmovile.presentation.screens.sudoku.SudokuScreen
import com.google.gson.Gson

sealed class Screen(
    val route: String,
) {
    object Home : Screen("home")

    object Sudoku : Screen("sudoku/{size}/{difficulty}") {
        fun createRoute(
            size: Int,
            difficulty: String,
        ) = "sudoku/$size/$difficulty"
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ExamenNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val gson = remember { Gson() }
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onClick = { size, difficulty ->
                    navController.navigate(Screen.Sudoku.createRoute(size, difficulty))
                },
            )
        }

        composable(
            route = Screen.Sudoku.route,
            arguments =
                listOf(
                    navArgument("size") { type = NavType.IntType },
                    navArgument("difficulty") { type = NavType.StringType },
                ),
        ) { backStackEntry ->
            val size = backStackEntry.arguments?.getInt("size") ?: 4
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: ""
            SudokuScreen(
                size = size,
                difficulty = difficulty,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
