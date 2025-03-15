/*
 * Assignment 6
 * NavGraph.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * OSU
 * CS 492
 */

package com.example.treasurehunt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.treasurehunt.TreasureHuntViewModel
import com.example.treasurehunt.screens.ClueScreen
import com.example.treasurehunt.screens.ClueSolvedScreen
import com.example.treasurehunt.screens.CompletionScreen
import com.example.treasurehunt.screens.PermissionsScreen
import com.example.treasurehunt.screens.StartScreen
import com.google.android.gms.location.FusedLocationProviderClient

object Routes {
    const val PERMISSIONS = "permissions"
    const val START = "start"
    const val CLUE = "clue"
    const val CLUE_SOLVED = "clue_solved"
    const val COMPLETION = "completion"
}

@Composable
fun TreasureNavGraph(
    navController: NavHostController,
    viewModel: TreasureHuntViewModel,
    modifier: Modifier = Modifier,
    fusedLocationClient: FusedLocationProviderClient
) {
    NavHost(
        navController = navController,
        startDestination = Routes.PERMISSIONS,
        modifier = modifier
    ) {
        composable(Routes.PERMISSIONS) {
            PermissionsScreen(
                onPermissionsGranted = {
                    navController.navigate(Routes.START) {
                        popUpTo(Routes.PERMISSIONS) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.START) {
            StartScreen(
                viewModel = viewModel,
                onStartClicked = {
                    navController.navigate(Routes.CLUE)
                }
            )
        }
        composable(Routes.CLUE) {
            ClueScreen(
                viewModel = viewModel,
                onFoundIt = { isCorrectLocation ->
                    if (isCorrectLocation) {
                        navController.navigate(Routes.CLUE_SOLVED)
                    } else {
                        // Stay on clue, maybe show a dialog or snack
                    }
                },
                onQuit = {
                    // Reset the game and go back to START
                    viewModel.quitGame()
                    navController.navigate(Routes.START) {
                        popUpTo(Routes.CLUE) { inclusive = true }
                    }
                },
                fusedLocationClient = fusedLocationClient
            )
        }
        composable(Routes.CLUE_SOLVED) {
            ClueSolvedScreen(
                viewModel = viewModel,
                onContinue = {
                    // Check if final
                    if (viewModel.isFinalClue()) {
                        navController.navigate(Routes.COMPLETION) {
                            popUpTo(Routes.CLUE_SOLVED) { inclusive = true }
                        }
                    } else {
                        // Next clue
                        viewModel.goToNextClue()
                        viewModel.resumeTimer()
                        navController.navigate(Routes.CLUE) {
                            popUpTo(Routes.CLUE_SOLVED) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Routes.COMPLETION) {
            CompletionScreen(
                viewModel = viewModel,
                onHome = {
                    viewModel.quitGame()
                    navController.navigate(Routes.START) {
                        popUpTo(Routes.COMPLETION) { inclusive = true }
                    }
                }
            )
        }
    }
}
