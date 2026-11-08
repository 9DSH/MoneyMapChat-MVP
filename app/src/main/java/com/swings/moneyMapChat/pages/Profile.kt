package com.swings.moneyMapChat.pages


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UserProfile(navController: NavController)
{

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xff003366),
                    titleContentColor = Color.White
                )
            )
        },
        content = { it
        }
    )
}