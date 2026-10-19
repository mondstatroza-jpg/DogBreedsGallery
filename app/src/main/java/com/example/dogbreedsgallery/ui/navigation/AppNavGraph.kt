package com.example.dogbreedsgallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dogbreedsgallery.ui.screens.DetailsScreen
import com.example.dogbreedsgallery.ui.screens.FavoritesScreen
import com.example.dogbreedsgallery.ui.screens.MainScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onBreedClick = { id -> navController.navigate("details/$id") },
                onFavoritesClick = { navController.navigate("favorites") }
            )
        }
        composable(
            "details/{breedId}",
            arguments = listOf(navArgument("breedId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("breedId") ?: 0
            DetailsScreen(breedId = id, onBackClick = { navController.popBackStack() })
        }
        composable("favorites") {
            FavoritesScreen(
                onBreedClick = { id -> navController.navigate("details/$id") },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}