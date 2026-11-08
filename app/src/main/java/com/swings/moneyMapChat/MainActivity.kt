package com.swings.moneyMapChat

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nikolovlazar.moneyMapChat.R
import com.swings.moneyMapChat.ui.theme.GoodbyeMoneyTheme
import com.swings.moneyMapChat.ui.theme.TopAppBarBackground
import com.swings.moneyMapChat.assistant.gpt.MainViewModel
import com.swings.moneyMapChat.assistant.gpt.MyGPT
import com.swings.moneyMapChat.pages.Add
import com.swings.moneyMapChat.pages.Categories
import com.swings.moneyMapChat.pages.Expenses
import com.swings.moneyMapChat.pages.Reports
import com.swings.moneyMapChat.pages.Settings
import com.swings.moneyMapChat.profile.ProfileScreen
import io.sentry.compose.withSentryObservableEffect
import io.realm.kotlin.Realm
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity(){
  private val viewModel: MainViewModel by viewModels()

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    val realm = Realm.open(config)
    installSplashScreen()
    setContent {
      GoodbyeMoneyTheme {
        var showBottomBar by rememberSaveable { mutableStateOf(true) }
        var showSplashScreen by rememberSaveable { mutableStateOf(true) }
        val navController = rememberNavController().withSentryObservableEffect()
        val backStackEntry by navController.currentBackStackEntryAsState()

        showBottomBar = !showSplashScreen && when (backStackEntry?.destination?.route) {
          "settings/categories" -> false
          else -> true
        }

        Scaffold(
          bottomBar = {
            if (showBottomBar) {
              NavigationBar(containerColor = TopAppBarBackground) {
                NavigationBarItem(
                  selected = backStackEntry?.destination?.route == "expenses",
                  onClick = { navController.navigate("expenses") },
                  label = {
                    Text("Expenses")
                  },
                  icon = {
                    Icon(
                      painterResource(id = R.drawable.upload),
                      contentDescription = "Upload"
                    )
                  }
                )
                NavigationBarItem(
                  selected = backStackEntry?.destination?.route == "reports",
                  onClick = { navController.navigate("reports") },
                  label = {
                    Text("Reports")
                  },
                  icon = {
                    Icon(
                      painterResource(id = R.drawable.bar_chart),
                      contentDescription = "Reports"
                    )
                  }
                )
                NavigationBarItem(
                  selected = backStackEntry?.destination?.route == "mygpt",
                  onClick = { navController.navigate("mygpt") },
                  label = {
                    Text("Assistant")
                  },
                  icon = {
                    Icon(
                      painterResource(id = R.drawable.logo_withe),
                      contentDescription = "Assistant",
                      modifier= Modifier.size(24.dp)
                    )
                  }
                )

//                NavigationBarItem(
//                  selected = backStackEntry?.destination?.route == "add",
//                  onClick = { navController.navigate("add") },
//                  label = {
//                    Text("Add")
//                  },
//                  icon = {
//                    Icon(
//                      painterResource(id = R.drawable.add),
//                      contentDescription = "Add"
//                    )
//                  }
//                )

                NavigationBarItem(
                  selected = backStackEntry?.destination?.route == "profile",
                  onClick = { navController.navigate("profile") },
                  label = {
                    Text("Profile")
                  },
                  icon = {
                    Icon(
                      painterResource(id = R.drawable.baseline_account_circle_24),
                      contentDescription = "Profile"
                    )
                  }
                )


                NavigationBarItem(
                  selected = backStackEntry?.destination?.route?.startsWith("settings")
                    ?: false,
                  onClick = { navController.navigate("settings") },
                  label = {
                    Text("Settings")
                  },
                  icon = {
                    Icon(
                      painterResource(id = R.drawable.settings_outlined),
                      contentDescription = "Settings"
                    )
                  }
                )


              }
            }
          },
          content = { innerPadding ->
            NavHost(
              navController = navController,
              startDestination = "splash"
            ) {
              composable("splash") {
                SplashScreen(navController)
                LaunchedEffect(key1 = true) {
                  delay(2000) // Match the splash screen duration
                  showSplashScreen = false
                }
              }
              composable("expenses") {
                Surface(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                ) {
                  Expenses(navController)
                }
              }
              composable("reports") {
                Surface(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                ) {
                  Reports()
                }
              }
              composable("mygpt") {
                Surface(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                ) {
                  MyGPT(viewModel, realm = realm)
                }
              }

              composable("add") {
                Surface(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                ) {
                  Add(navController)
                }
              }

              composable("profile") {
                Surface(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                ) {
                  ProfileScreen()
                }
              }



              composable("settings") {
                Surface(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                ) {
                  Settings(navController)
                }
              }
              composable("settings/categories") {
                Surface(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                ) {
                  Categories(navController)
                }
              }
            }
          }
        )
      }
    }
  }

}

