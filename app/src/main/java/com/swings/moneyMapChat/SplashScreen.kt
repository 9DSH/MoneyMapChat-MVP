package com.swings.moneyMapChat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.nikolovlazar.moneyMapChat.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Customize background color
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_white), // Replace with your logo
            contentDescription = "App Logo"
        )
    }

    // Trigger onTimeout after a delay
    LaunchedEffect(key1 = true) {
        delay(2000) // Adjust delay as needed
        navController.popBackStack() // Remove the splash screen from the back stack
        navController.navigate("expenses")
    }
}