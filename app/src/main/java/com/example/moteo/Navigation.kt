package com.example.moteo

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun MoteoNavGraph(
    navController: NavHostController,
    context: Context,
    apiService: ApiService,
    userPreferences: UserPreferences
) {
    NavHost(navController = navController, startDestination = "accueil") {

        composable("accueil") {
            AccueilScreen(
                navController = navController,
                apiService = apiService,
                userPreferences = userPreferences
            )
        }

        composable("inscription") {
            InscriptionScreen(
                navController = navController,
                apiService = apiService,
                userPreferences = userPreferences
            )
        }

        composable(
            route = "utilisateur/{pseudo}",
            arguments = listOf(navArgument("pseudo") { type = NavType.StringType })
        ) { backStackEntry ->
            val pseudo = backStackEntry.arguments?.getString("pseudo") ?: ""

            UtilisateurScreen(
                navController = navController,
                pseudo = pseudo
            )
        }
    }
}
