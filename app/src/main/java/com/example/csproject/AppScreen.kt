package com.example.csproject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.csproject.ui.StartScreen
import com.example.csproject.ui.OperatingInstructionsMenuScreen
import com.example.csproject.ui.TransactionListScreen
import com.example.csproject.ui.theme.CSProjectTheme

enum class AppScreen(val title: String) {
    Onboarding(title = "Onboarding"),
    TransactionView(title = "Transaction List"),
    OperatingInstructionsMenu(title = "Help"),
}

@Composable
fun AppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CSProjectTheme {
        TopAppBar {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = currentScreen.title,
                    color = Color.White,
                    style = MaterialTheme.typography.h3
                )
            }
        }
    }
}

@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    //viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Onboarding.name
    )

    Scaffold(
        /*
        topBar = {
            AppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
         */
    ) { innerPadding ->
        //val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = AppScreen.Onboarding.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Onboarding.name) {
                StartScreen(
                    _topBar = {},
                    gotoAppButtonAction = { navController.navigate(AppScreen.TransactionView.name) },
                    gotoOperatingInstructionsActivityButtonAction = { navController.navigate(AppScreen.OperatingInstructionsMenu.name) },
                )
            }
            composable(route = AppScreen.TransactionView.name) {
                TransactionListScreen(
                    _topBar = {
                        AppBar(
                            currentScreen = currentScreen,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() }
                        )
                    }
                )
            }
            composable(route = AppScreen.OperatingInstructionsMenu.name) {
                OperatingInstructionsMenuScreen(
                    _topBar = {
                        AppBar(
                            currentScreen = currentScreen,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() }
                        )
                    },
                    gotoLoggingHelpButtonAction = {navController.navigate(AppScreen.TransactionView.name)},
                    LoggingHelpButtonIconId = R.drawable.money_sign,
                    gotoCategorisationHelpButtonAction = {navController.navigate(AppScreen.TransactionView.name)},
                    CategorisationHelpButtonIconId = R.drawable.categorisation_sign,
                    gotoGraphingHelpButtonAction = {navController.navigate(AppScreen.TransactionView.name)},
                    GraphingHelpButtonIconId = R.drawable.graph_sign,
                    gotoSettingsButtonAction = {navController.navigate(AppScreen.TransactionView.name)},
                    SettingsButtonButtonIconId = R.drawable.ic_baseline_settings_50,
                    gotoStartScreenButtonAction = {
                        while(navController.popBackStack()) {}
                        navController.navigate(AppScreen.Onboarding.name)
                    },
                )
            }

        }
    }
}