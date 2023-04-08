package com.example.moneymind

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moneymind.ViewModels.GraphScreenViewModel
import com.example.moneymind.ViewModels.HelpScreenViewModel
import com.example.moneymind.ViewModels.TransactionCategoryViewModel
import com.example.moneymind.ViewModels.TransactionLogViewModel
import com.example.moneymind.ui.*
import com.example.moneymind.ui.theme.CSProjectTheme
import com.example.moneymind.ui.theme.DarkLibreOfficeBlue

enum class AppScreen(val title: String) {
    Onboarding(title = "Onboarding"),
    GeneralTransactionView(title = "Transaction List"),
    TransactionLogsScreen(title = "Transaction List 2"),
    OperatingInstructionsMenu(title = "Help"),
    Settings(title = "Operating Instructions"),
    CategoriesScreen(title = "Categories List"),
    GraphingScreen(title = "Transaction Graphs"),
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
fun CustomAppBar(
    navController: NavHostController,
    backClick : () -> Unit,
    ellipsesClick : () -> Unit,
){
    var showMenu by remember { mutableStateOf(false) }

    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            TextButton(
                onClick = backClick,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(30)
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = RoundedCornerShape(30)
                    ),
            ) {
                Text(
                    "< Back",
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
            ) {

                TextButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .background(
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(30)
                        )
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colors.primaryVariant,
                            shape = RoundedCornerShape(30)
                        ),
                ) {
                    Text(
                        "...",
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.Center
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(color = MaterialTheme.colors.surface)
                ) {
                    MyDropdownMenuItems(navController = navController)
                }
            }
        }

        Divider(
            color = DarkLibreOfficeBlue,
            modifier = Modifier.height(3.dp)
        )
    }
}

@Composable
fun MyDropdownMenuItems( navController: NavHostController ){
    Column(modifier = Modifier.background(color = Color.Transparent)) {
        DropdownMenuItem(onClick = { navController.navigate(AppScreen.OperatingInstructionsMenu.name) }) {
            Text(text = "Operating Instructions")
        }

        Spacer(modifier = Modifier.height(2.dp))
        Divider(
            color = DarkLibreOfficeBlue,
            modifier = Modifier
                .fillMaxWidth()  //fill the max height
                .height(2.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))

        DropdownMenuItem(onClick = {
            navController.navigate(AppScreen.Settings.name)
        }) {
            Text(text = "Settings")
        }
    }


}

@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    helpScreenViewModel: HelpScreenViewModel = viewModel(),
    transactionLogsViewModel: TransactionLogViewModel = viewModel(),
    transactionCategoriesViewModel: TransactionCategoryViewModel = viewModel(),
    graphScreenViewModel : GraphScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Onboarding.name
    )
    val transactionCategoriesState by remember { mutableStateOf(transactionCategoriesViewModel.uiState) }
    val context = LocalContext.current


    //Log.d("um","")

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
                    gotoAppButtonAction = {
                        while (navController.popBackStack()) { }
                        navController.navigate(AppScreen.GeneralTransactionView.name)
                    },
                    gotoOperatingInstructionsActivityButtonAction = { navController.navigate(AppScreen.OperatingInstructionsMenu.name) },
                )
            }
            composable(route = AppScreen.GeneralTransactionView.name) {
                GeneralTransactionViewScreen(
                    _topBar = {

                        var showMenu by remember { mutableStateOf(false) }

                        Column() {
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.TopCenter)
                                    .padding(10.dp)
                            ){
                                var showExpandedAppBar by remember{ mutableStateOf(false) }
                                var collapseExpandText by remember{ mutableStateOf("Expand") }

                                if(showExpandedAppBar) {
                                    TextButton(
                                        onClick = { navController.navigate(AppScreen.TransactionLogsScreen.name) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = MaterialTheme.colors.primary,
                                                shape = RoundedCornerShape(30)
                                            )
                                            .border(
                                                width = 3.dp,
                                                color = MaterialTheme.colors.primaryVariant,
                                                shape = RoundedCornerShape(30)
                                            ),
                                    ) {
                                        Text(
                                            "Transaction Logs",
                                            style = MaterialTheme.typography.button,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(5.dp))

                                    TextButton(
                                        onClick = { navController.navigate(AppScreen.GraphingScreen.name) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = MaterialTheme.colors.primary,
                                                shape = RoundedCornerShape(30)
                                            )
                                            .border(
                                                width = 3.dp,
                                                color = MaterialTheme.colors.primaryVariant,
                                                shape = RoundedCornerShape(30)
                                            ),
                                    )
                                    {
                                        Text(
                                            "Graphs",
                                            style = MaterialTheme.typography.button,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(5.dp))
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                ) {
                                    TextButton(
                                        onClick = {
                                            if (collapseExpandText == "Expand") {
                                                showExpandedAppBar = true
                                                collapseExpandText = "Collapse"
                                            } else {
                                                showExpandedAppBar = false
                                                collapseExpandText = "Expand"
                                            }
                                        },
                                        modifier = Modifier
                                            .width(((context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density - 3 * 10) * (3.0 / 4.0)).dp)
                                            .background(
                                                color = MaterialTheme.colors.primary,
                                                shape = RoundedCornerShape(30)
                                            )
                                            .border(
                                                width = 3.dp,
                                                color = MaterialTheme.colors.primaryVariant,
                                                shape = RoundedCornerShape(30)
                                            ),
                                    ){
                                        Text(collapseExpandText, style = MaterialTheme.typography.button, textAlign = TextAlign.Center)
                                    }
                                    
                                    Spacer(modifier = Modifier.width(10.dp))

                                    Row() {
                                        TextButton(
                                            onClick = { showMenu = !showMenu },
                                            modifier = Modifier
                                                .width(((context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density - 3 * 10) * (1.0 / 4.0)).dp)
                                                .background(
                                                    color = MaterialTheme.colors.primary,
                                                    shape = RoundedCornerShape(30)
                                                )
                                                .border(
                                                    width = 3.dp,
                                                    color = MaterialTheme.colors.primaryVariant,
                                                    shape = RoundedCornerShape(30)
                                                ),
                                        ) {
                                            Text(
                                                "...",
                                                style = MaterialTheme.typography.button,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = showMenu,
                                            onDismissRequest = { showMenu = false },
                                            modifier = Modifier.background(color = MaterialTheme.colors.surface)
                                        ) {
                                            MyDropdownMenuItems(navController = navController)
                                        }
                                    }
                                }
                            }

                            Divider(
                                color = DarkLibreOfficeBlue,
                                modifier = Modifier.height(3.dp)
                            )
                        }
                    },
                    transactionsLogViewModel = transactionLogsViewModel,
                    transactionCategoryViewModel = transactionCategoriesViewModel
                )
            }
            composable(route = AppScreen.GraphingScreen.name) {
                GraphsScreen(
                    _topBar = {
                        CustomAppBar(
                            backClick = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.navigateUp()
                                } else {
                                    navController.navigate(AppScreen.GeneralTransactionView.name)
                                }
                            },
                            ellipsesClick = {},
                            navController = navController
                        )
                    },
                    transactionsLogViewModel = transactionLogsViewModel,
                    transactionCategoryViewModel = transactionCategoriesViewModel,
                    graphScreenViewModel = graphScreenViewModel
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
                        // navController.navigate(AppScreen.OperatingInstructions.name)
                        // helpScreenViewModel.setHelpScreenOption("Logging")
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.logging_help_url)))
                        context.startActivity(intent)
                    },
                    gotoCategorisationHelpButtonAction = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.categorising_help_url)))
                        context.startActivity(intent)
                    },
                    gotoGraphingHelpButtonAction = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.graphing_help_url)))
                        context.startActivity(intent)
                    },
                    gotoSettingsButtonAction = {
                        navController.navigate(AppScreen.Settings.name)
                        helpScreenViewModel.setHelpScreenOption("Settings")
                    },
                    gotoStartScreenButtonAction = {
                        while (navController.popBackStack()) { }
                        navController.navigate(AppScreen.Onboarding.name)
                    },
                )
            }
            composable(route = AppScreen.Settings.name) {
                SettingsScreen(
                    helpOption = "Settings",
                    _topBar = {
                        customTitleAppBar(
                            title = "Settings",
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
                    transactionLogViewModel = transactionLogsViewModel
                )
            }

            composable(route = AppScreen.TransactionLogsScreen.name) {
                TransactionLogsScreen(
                    _topBar = {
                        CustomAppBar(
                            backClick = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.navigateUp()
                                } else {
                                    navController.navigate(AppScreen.GeneralTransactionView.name)
                                }
                            },
                            ellipsesClick = {},
                            navController = navController
                        )
                    },
                    _gotoCategoriesScreen = {navController.navigate(AppScreen.CategoriesScreen.name)},
                    transactionsLogViewModel = transactionLogsViewModel,
                    transactionCategoryViewModel = transactionCategoriesViewModel
                )
            }

            composable(route = AppScreen.CategoriesScreen.name) {
                CategoriesScreen(
                    _topBar = {
                        CustomAppBar(
                            backClick = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.navigateUp()
                                } else {
                                    navController.navigate(AppScreen.GeneralTransactionView.name)
                                }
                            },
                            ellipsesClick = {},
                            navController = navController
                        )
                    },
                    transactionsLogViewModel = transactionLogsViewModel,
                    transactionCategoryViewModel = transactionCategoriesViewModel
                )
            }
        }
    }
}