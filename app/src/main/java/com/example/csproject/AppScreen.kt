package com.example.csproject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.csproject.ViewModels.HelpScreenViewModel
import com.example.csproject.ViewModels.TransactionLogViewModel
import com.example.csproject.ui.StartScreen
import com.example.csproject.ui.OperatingInstructionsMenuScreen
import com.example.csproject.ui.OperatingInstructionsScreen
import com.example.csproject.ui.TransactionListScreen
import com.example.csproject.ui.theme.CSProjectTheme

enum class AppScreen(val title: String) {
    Onboarding(title = "Onboarding"),
    TransactionView(title = "Transaction List"),
    OperatingInstructionsMenu(title = "Help"),
    OperatingInstructions(title = "Operating Instructions"),
}

@Composable
fun AppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    customTitleAppBar(title = currentScreen.title, currentScreen = currentScreen, canNavigateBack = canNavigateBack, navigateUp = navigateUp)
}

@Composable
fun customTitleAppBar(
    title: String,
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
                    text = title,
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
    helpScreenViewModel: HelpScreenViewModel = viewModel(),
    transactionsLogViewModel: TransactionLogViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Onboarding.name
    )

    Scaffold { innerPadding ->
        val helpScreenState by helpScreenViewModel.uiState.collectAsState()


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
                    },
                    transactionsLogViewModel = transactionsLogViewModel
                )
            }
            composable(route = AppScreen.OperatingInstructionsMenu.name){
                OperatingInstructionsMenuScreen(
                    _topBar = {
                        AppBar(
                            currentScreen = currentScreen,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() }
                        )
                    },
                    gotoLoggingHelpButtonAction = {
                        navController.navigate(AppScreen.OperatingInstructions.name)
                        helpScreenViewModel.setHelpScreenOption("Logging")
                    },
                    gotoCategorisationHelpButtonAction = {
                        navController.navigate(AppScreen.OperatingInstructions.name)
                        helpScreenViewModel.setHelpScreenOption("Categorisation")
                    },
                    gotoGraphingHelpButtonAction = {
                        navController.navigate(AppScreen.OperatingInstructions.name)
                        helpScreenViewModel.setHelpScreenOption("Graphing")
                    },
                    gotoSettingsButtonAction = {
                        navController.navigate(AppScreen.OperatingInstructions.name)
                        helpScreenViewModel.setHelpScreenOption("Settings")
                    },
                    gotoStartScreenButtonAction = {
                        while (navController.popBackStack()) { }
                        navController.navigate(AppScreen.Onboarding.name)
                    },
                )
            }
            composable(route = AppScreen.OperatingInstructions.name) {
                OperatingInstructionsScreen(
                    helpOption = helpScreenState.helpOption,
                    _topBar = {
                        customTitleAppBar(
                            title = helpScreenState.helpOption,
                            currentScreen = currentScreen,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() }
                        )
                    },
                    gotoPreviousScreenAction = {
                        if( navController.previousBackStackEntry != null) {
                            navController.navigateUp()
                        }
                        else {
                            navController.navigate(AppScreen.OperatingInstructionsMenu.name)
                        }
                    },
                )
            }


        }
    }
}