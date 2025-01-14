package com.phenix.isix.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phenix.isix.ui.screen.DetailsScreen
import com.phenix.isix.ui.screen.HomeScreen
import com.phenix.isix.ui.screen.Other2Screen
import com.phenix.isix.ui.screen.OtherScreen


@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController,
        startDestination = "home",
        modifier
    ) {
        composable("home") { HomeScreen() }
        composable("details") { DetailsScreen() }
        composable("other") { OtherScreen() }
        composable("other2") { Other2Screen() }
    }
}